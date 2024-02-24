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

@file:Suppress("DEPRECATION")

package dev.azn9.plugins.discord.settings.options.impl

import dev.azn9.plugins.discord.settings.options.OptionHolder
import dev.azn9.plugins.discord.settings.options.types.Option
import org.jdom.Element
import javax.swing.JComponent

@Suppress("DEPRECATION")
open class HiddenOptionHolderImpl : OptionHolder {
    override val options = LinkedHashMap<String, Option<*>>()

    override val component: JComponent? = null

    fun readExternal(element: Element) {
        for ((key, option) in options) {
            option.readXml(element, key)
        }
    }

    fun writeExternal(element: Element) {
        for ((key, option) in options) {
            // if (!option.isDefault) {
            option.writeXml(element, key)
            // }
        }
    }
}
