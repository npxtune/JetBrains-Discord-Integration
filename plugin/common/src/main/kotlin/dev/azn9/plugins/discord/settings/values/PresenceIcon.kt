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

package dev.azn9.plugins.discord.settings.values

import dev.azn9.plugins.discord.render.RenderContext
import dev.azn9.plugins.discord.settings.options.types.SimpleValue
import dev.azn9.plugins.discord.settings.options.types.UiValueType
import dev.azn9.plugins.discord.icons.source.Asset as SourceAsset

typealias IconValue = SimpleValue<PresenceIcon>

enum class PresenceIcon(override val text: String, override val description: String? = null) : RenderedValue<PresenceIcon.Result>, UiValueType {
    PROJECT("Project") {
        override fun RenderContext.getResult() = icons?.getAsset("project").toResult()
    },
    APPLICATION("Application") {
        override fun RenderContext.getResult() = icons?.getAsset("application").toResult()
    },
    FILE("File") {
        override fun RenderContext.getResult(): Result {
            return icons?.let { icons -> language?.findIcon(icons) }?.asset.toResult()
        }
    },
    NONE("None") {
        override fun RenderContext.getResult() = Result.Empty
    };

    object Large {
        val Application = APPLICATION to arrayOf(APPLICATION, NONE)
        val Project = PROJECT to arrayOf(PROJECT, APPLICATION, NONE)
        val File = FILE to arrayOf(PROJECT, APPLICATION, FILE, NONE)
    }

    object Small {
        val Application = NONE to arrayOf(APPLICATION, NONE)
        val Project = NONE to arrayOf(APPLICATION, NONE)
        val File = PROJECT to arrayOf(PROJECT, APPLICATION, FILE, NONE)
    }

    fun SourceAsset?.toResult() = when (this) {
        null -> Result.Empty
        else -> Result.Asset(this)
    }

    sealed class Result {
        object Empty : Result()
        data class Asset(val value: SourceAsset) : Result()
    }
}
