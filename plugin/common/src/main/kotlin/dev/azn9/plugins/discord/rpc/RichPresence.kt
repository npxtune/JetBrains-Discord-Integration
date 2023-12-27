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

import com.almightyalpaca.jetbrains.plugins.discord.icons.source.Asset
import dev.azn9.plugins.discord.utils.*
import java.awt.image.BufferedImage
import java.net.URL
import java.time.OffsetDateTime
import javax.imageio.ImageIO

class RichPresence(
    val appId: Long?,
    state: String? = null,
    details: String? = null,
    var startTimestamp: OffsetDateTime? = null,
    var endTimestamp: OffsetDateTime? = null,
    var largeImage: Image? = null,
    var smallImage: Image? = null,
    partyId: String? = null,
    var instance: Boolean = false,
    var button1Title: String? = null,
    var button1Url: String? = null,
    var button2Title: String? = null,
    var button2Url: String? = null
) {
    constructor(appId: Long?, initializer: RichPresence.() -> Unit) : this(appId) {
        if (appId != null) {
            initializer()
        }
    }

    var state by limitingLength(state, 2..128, true)
    var details by limitingLength(details, 2..128, true)
    var partyId by verifyingLength(partyId, 0..128)
    // TODO : validate length for button titles and urls

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RichPresence) return false

        if (appId != other.appId) return false
        if (startTimestamp != other.startTimestamp) return false
        if (endTimestamp != other.endTimestamp) return false
        if (largeImage != other.largeImage) return false
        if (smallImage != other.smallImage) return false
        if (instance != other.instance) return false
        if (state != other.state) return false
        if (details != other.details) return false
        if (partyId != other.partyId) return false
        if (button1Title != other.button1Title) return false
        if (button1Url != other.button1Url) return false
        if (button2Title != other.button2Title) return false
        if (button2Url != other.button2Url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = appId?.hashCode() ?: 0

        result = 31 * result + (startTimestamp?.hashCode() ?: 0)
        result = 31 * result + (endTimestamp?.hashCode() ?: 0)
        result = 31 * result + (largeImage?.hashCode() ?: 0)
        result = 31 * result + (smallImage?.hashCode() ?: 0)
        result = 31 * result + instance.hashCode()
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + (details?.hashCode() ?: 0)
        result = 31 * result + (partyId?.hashCode() ?: 0)
        result = 31 * result + (button1Title?.hashCode() ?: 0)
        result = 31 * result + (button1Url?.hashCode() ?: 0)
        result = 31 * result + (button2Title?.hashCode() ?: 0)
        result = 31 * result + (button2Url?.hashCode() ?: 0)

        return result
    }

    class Image(var asset: Asset?, text: String?) {
        val key = asset?.id?.limit(0..32, false)
        var text by limitingLength(text, 2..128, true)

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Image) return false

            if (asset != other.asset) return false
            if (key != other.key) return false

            return true
        }

        override fun hashCode(): Int {
            var result = asset?.hashCode() ?: 0
            result = 31 * result + (key?.hashCode() ?: 0)
            return result
        }
    }
}

sealed class User {
    abstract val name: String
    abstract val tag: String?
    abstract fun getAvatar(size: Int? = null): BufferedImage?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (name != other.name) return false
        if (tag != other.tag) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (tag?.hashCode() ?: 0)
        return result
    }

    class Normal(override val name: String, override val tag: String, val id: Long, val avatarId: String?) : User() {
        override fun getAvatar(size: Int?): BufferedImage? {
            val fixedSize = when (size) {
                null -> 128
                else -> size.roundToNextPowerOfTwo().coerceIn(16..4096)
            }

            return when (avatarId) {
                null, "" -> URL("https://cdn.discordapp.com/embed/avatars/${(tag.toInt() % 5)}.png?size=$fixedSize")
                else -> URL("https://cdn.discordapp.com/avatars/$id/$avatarId.png?size=$fixedSize")
            }.getImage()
        }
    }

    object CLYDE : User() {
        override val name = "Clyde"
        override val tag: String? = null

        // URL: https://discordapp.com/assets/f78426a064bc9dd24847519259bc42af.png
        override fun getAvatar(size: Int?): BufferedImage =
            ImageIO.read(CLYDE::class.java.getResourceAsStream("/discord/images/avatars/clyde.png"))
    }

    companion object
}
