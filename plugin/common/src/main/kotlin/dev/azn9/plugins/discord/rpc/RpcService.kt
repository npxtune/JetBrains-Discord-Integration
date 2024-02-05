/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2023 Axel JOLY (Azn9) <contact@azn9.dev>
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

package dev.azn9.plugins.discord.rpc

import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.rpc.connection.DiscordConnection
import dev.azn9.plugins.discord.rpc.connection.DiscordIpcConnection
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProcessCanceledException
import com.intellij.openapi.util.Disposer
import dev.azn9.plugins.discord.utils.debugLazy
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

val rpcService: RpcService
    get() = service()

typealias UserCallback = (User?) -> Unit

@Service
class RpcService : DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    private var _user: User? = null
    val user: User
        get() = _user ?: User.CLYDE

    private var connection: DiscordConnection? = null

    private var lastPresence: RichPresence? = null

    private var connectionChecker: Job? = null

    private val mutex = Mutex()

    private fun checkConnected(): Job = launch {
        delay(20_000)

        mutex.withLock {
            DiscordPlugin.LOG.debug("Checking for running rpc connection")

            val connection = connection ?: return@launch

            if (!connection.running) {
                DiscordPlugin.LOG.debug("Rpc connection not running, reconnecting")

                update(lastPresence, forceReconnect = true)
            } else {
                DiscordPlugin.LOG.debug("Rpc connection is running")

                checkConnected()
            }
        }
    }

    private fun updateUser(user: User?) {
        _user = user
    }

    fun update(presence: RichPresence?, forceUpdate: Boolean = false, forceReconnect: Boolean = false) = launch {
        DiscordPlugin.LOG.debug("Called .update , islocked=${mutex.isLocked}")

        throw IllegalStateException("This is a test") // TODO: remove

        val exceptionHandler = CoroutineExceptionHandler { _, error ->
            when (error) {
                is ProcessCanceledException -> {
                    DiscordPlugin.LOG.warn("PCE while updating presence", error)
                }

                else -> {
                    DiscordPlugin.LOG.warn("Error while updating presence", error)
                }
            }
        }

        mutex.withLock {
            launch(exceptionHandler) mutex@ {
                DiscordPlugin.LOG.debug("Updating presence, forceUpdate=$forceUpdate, forceReconnect=$forceReconnect")

                if (!(forceUpdate || forceReconnect) && lastPresence != null) {
                    if (lastPresence == presence) {
                        DiscordPlugin.LOG.debug("Presence unchanged, skipping update")
                        return@mutex
                    }

                    lastPresence = presence
                }

                if (presence?.appId == null) { // Stop connection
                    when (presence) {
                        null -> DiscordPlugin.LOG.debug("Presence null, stopping connection")
                        else -> DiscordPlugin.LOG.debug("Presence.appId null, stopping connection")
                    }

                    if (connection != null) {
                        connectionChecker?.cancel()
                        connectionChecker = null
                        connection?.disconnect()
                        connection = null
                    }

                    return@mutex
                } else {
                    if (forceReconnect || connection?.appId != presence.appId) {
                        when {
                            forceReconnect -> DiscordPlugin.LOG.debug("Forcing reconnect to client")
                            connection == null -> DiscordPlugin.LOG.debug("Connecting to client")
                            else -> DiscordPlugin.LOG.debug("Reconnecting to client due to changed appId")
                        }

                        if (connection != null) {
                            connectionChecker?.cancel()
                            connectionChecker = null
                            connection?.run(Disposer::dispose)
                            connection = null
                        }

                        connection = createConnection(presence.appId).apply {
                            Disposer.register(this@RpcService, this@apply)
                            connect()
                        }

                        connectionChecker = checkConnected()
                    }

                    withTimeoutOrNull(4500) {
                        connection?.send(presence)
                    }
                }
            }
        }
    }

    override fun dispose() {
        runBlocking { update(null) }

        super.dispose()
    }

    private fun createConnection(appId: Long): DiscordConnection {
        return DiscordIpcConnection(appId, ::updateUser)
    }
}
