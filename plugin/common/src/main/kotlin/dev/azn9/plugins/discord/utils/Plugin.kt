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

package dev.azn9.plugins.discord.utils

import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId

object Plugin {
    val pluginId: PluginId? by lazy { PluginId.getId("dev.azn9.plugins.discord") }
    private val plugin: IdeaPluginDescriptor? by lazy { PluginManagerCore.getPlugin(pluginId) }

    fun getId() = pluginId?.idString

    val version: Version? by lazy { plugin?.version?.let { Version(it) } }

    val branchBase = "https://github.com/Azn9/JetBrains-Discord-Integration/blob/" + when (val version = version) {
        null -> "master"
        else -> "v" + version.lastStable
    }

    class Version(private val asString: String) {
        val lastStable = asString.substringBefore("+")

        override fun toString(): String = asString

        fun isStable() = asString.matches(Regex("""\d+\.\d+\.\d+"""))
    }
}
