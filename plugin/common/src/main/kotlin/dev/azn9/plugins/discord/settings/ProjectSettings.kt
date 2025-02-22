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

package dev.azn9.plugins.discord.settings

import dev.azn9.plugins.discord.settings.options.OptionHolder
import dev.azn9.plugins.discord.settings.options.types.BooleanValue
import dev.azn9.plugins.discord.settings.options.types.StringValue
import dev.azn9.plugins.discord.settings.options.types.ThemeValue
import dev.azn9.plugins.discord.settings.values.ProjectShowValue
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import org.jdom.Element

val Project.settings: ProjectSettings
    get() = service()

interface ProjectSettings : PersistentStateComponent<Element>, OptionHolder {
    val project: Project

    val show: ProjectShowValue

    val nameOverrideEnabled: BooleanValue
    val nameOverrideText: StringValue

    val description: StringValue

    val theme: ThemeValue?

    val button1Title: StringValue
    val button1Url: StringValue
    val button2Title: StringValue
    val button2Url: StringValue
}
