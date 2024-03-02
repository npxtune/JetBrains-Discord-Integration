/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2023-2024 Axel JOLY (Azn9) <contact@azn9.dev>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.azn9.plugins.discord.rpc.connection

import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.icons.source.Asset
import dev.azn9.plugins.discord.rpc.RichPresence
import dev.azn9.plugins.discord.rpc.User
import dev.azn9.plugins.discord.rpc.UserCallback
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import dev.azn9.plugins.discord.utils.debugLazy
import dev.azn9.plugins.discord.utils.errorLazy
import dev.azn9.plugins.discord.utils.warnLazy
import dev.cbyrne.kdiscordipc.KDiscordIPC
import dev.cbyrne.kdiscordipc.core.error.ConnectionError
import dev.cbyrne.kdiscordipc.core.event.impl.CurrentUserUpdateEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ErrorEvent
import dev.cbyrne.kdiscordipc.core.event.impl.ReadyEvent
import dev.cbyrne.kdiscordipc.data.activity.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.util.Base64
import javax.imageio.ImageIO
import dev.cbyrne.kdiscordipc.data.user.User as NativeUser

class DiscordIpcConnection(override val appId: Long, private val userCallback: UserCallback) :
    DiscordConnection, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    private var ipcClient: KDiscordIPC = KDiscordIPC(appId.toString()).apply {
        // listening to an event doesn't actually do anything async, so we can just block here
        runBlocking {
            on<ReadyEvent>(::onReady)
            on<ErrorEvent>(::onError)
            on<CurrentUserUpdateEvent>(::onCurrentUserUpdate)
        }
    }

    override val running by ipcClient::connected

    override suspend fun connect() {
        if (!ipcClient.connected) {
            DiscordPlugin.LOG.debug("Starting new ipc connection...")

            val exceptionHandler = CoroutineExceptionHandler { _, error ->
                if (error is ConnectionError) {
                    DiscordPlugin.LOG.warn("Error connecting to ipc", error)
                } else {
                    DiscordPlugin.LOG.error("Error connecting to ipc", error)
                }
            }

            launch(exceptionHandler) {
                withTimeoutOrNull(5000) {
                    ipcClient.connect()
                    DiscordPlugin.LOG.debug("Started new ipc connection")
                }
            }
        }
    }

    override suspend fun send(presence: RichPresence?) {
        DiscordPlugin.LOG.debugLazy { "Sending new presence" }

        try {
            if (running)
                ipcClient.activityManager.setActivity(presence?.toNative())
        } catch (e: TimeoutCancellationException) {
            DiscordPlugin.LOG.debugLazy { "Error sending presence, timed out" }
        } catch (e: Exception) {
            DiscordPlugin.LOG.warnLazy(e) { "Error sending presence, is the client running?" }
        }
    }

    override suspend fun disconnect() = disconnectInternal()

    private fun disconnectInternal() {
        DiscordPlugin.LOG.debug("Closing IPC connection...")

        val exceptionHandler = CoroutineExceptionHandler { _, error ->
            when (error) {
                is ConnectionError -> {
                    DiscordPlugin.LOG.warn("Error closing ipc connection", error)
                }

                is UninitializedPropertyAccessException -> {
                    DiscordPlugin.LOG.warn("Error closing ipc connection", error)
                }

                else -> {
                    DiscordPlugin.LOG.error("Error closing ipc connection", error)
                }
            }
        }

        launch(exceptionHandler) {
            withTimeoutOrNull(5000) {
                ipcClient.activityManager.clearActivity()
                ipcClient.disconnect()
                DiscordPlugin.LOG.debug("IPC connection closed")
            }
        }
    }

    override fun dispose() {
        disconnectInternal()

        super.dispose()
    }

    private fun onReady(event: ReadyEvent) {
        DiscordPlugin.LOG.info("IPC connected")

        userCallback(event.data.user.toGeneric())
    }

    private fun onError(event: ErrorEvent) {
        DiscordPlugin.LOG.errorLazy { "IPC error: ${event.data}" }
    }

    private fun onCurrentUserUpdate(event: CurrentUserUpdateEvent) {
        userCallback(event.data.toGeneric())
    }

    // TODO: Register once the library exposes this again
    // private fun onDisconnect(reason: String) {
    //     DiscordPlugin.LOG.info("IPC disconnected: $reason")
    //
    //     userCallback(null)
    // }
}

private fun NativeUser.toGeneric() = User.Normal(this.username, discriminator, this.id.toLong(), this.avatar)

private fun emptyLineHack(s: String?): String {
    if (s == null || s.length < 2) return "  "
    return s
}

private fun RichPresence.toNative() = activity(
    state = emptyLineHack(this@toNative.state),
    details = emptyLineHack(this@toNative.details),
) {
    this@toNative.startTimestamp?.let {
        this.timestamps(
            start = it.toEpochSecond(),
            end = this@toNative.endTimestamp?.toEpochSecond()
        )
    }

    this@toNative.largeImage?.asset?.let {
        this.largeImage(it.getUrl(), this@toNative.largeImage?.text)
    }
    this@toNative.smallImage?.asset?.let {
        this.smallImage(it.getUrl(), this@toNative.smallImage?.text)
    }

    if ((this@toNative.button1Title ?: "") != "" && (this@toNative.button1Url ?: "") != "") {
        this.button(
            label = this@toNative.button1Title!!,
            url = this@toNative.button1Url!!
        )
    }
    if ((this@toNative.button2Title ?: "") != "" && (this@toNative.button2Url ?: "") != "") {
        this.button(
            label = this@toNative.button2Title!!,
            url = this@toNative.button2Url!!
        )
    }

    this.instance = this@toNative.instance
}
