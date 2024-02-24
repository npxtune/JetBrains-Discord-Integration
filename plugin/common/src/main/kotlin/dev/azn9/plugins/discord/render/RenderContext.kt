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

package dev.azn9.plugins.discord.render

import dev.azn9.plugins.discord.icons.source.IconSet
import dev.azn9.plugins.discord.icons.source.Source
import dev.azn9.plugins.discord.data.Data
import dev.azn9.plugins.discord.settings.options.types.SimpleValue
import dev.azn9.plugins.discord.settings.settings

class RenderContext(val source: Source, val data: Data, val mode: Renderer.Mode) {
    val icons: IconSet? by lazy {
        var themeValue = projectData?.projectSettings?.theme ?: settings.theme
        if (themeValue.getValue() == "default") {
            themeValue = settings.theme
        }

        source.getThemesOrNull()
            ?.get(themeValue.getValue())
            ?.getIconSet(settings.applicationType.getValue().applicationName)
    }

    val idleData = data as? Data.Idle
    val applicationData = data as? Data.Application
    val projectData = data as? Data.Project
    val fileData = data as? Data.File

    val language by lazy { fileData?.let { source.getLanguagesOrNull()?.findLanguage(fileData) } }

    fun createRenderer(): Renderer? {
        val type = when (data) {
            is Data.None -> Renderer.Type.None
            is Data.Idle -> Renderer.Type.Idle
            is Data.File -> Renderer.Type.File
            is Data.Project -> Renderer.Type.Project
            is Data.Application -> Renderer.Type.Application
        }

        return type.createRenderer(this)
    }

    fun <T> SimpleValue<T>.getValue(): T = getValue(mode)
}
