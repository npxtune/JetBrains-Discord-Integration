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

package dev.azn9.plugins.discord.icons.source.classpath

import dev.azn9.plugins.discord.icons.source.Asset
import dev.azn9.plugins.discord.icons.source.Theme
import dev.azn9.plugins.discord.icons.source.abstract.AbstractIconSet

class ClasspathIconSet(private val source: ClasspathSource, theme: Theme, applicationId: Long?, icons: Set<String>, applicationName: String) :
    AbstractIconSet(theme, applicationId, icons, applicationName) {
    override fun getAsset(assetId: String): Asset? = when (assetId in this) {
        true -> ClasspathAsset(source, assetId, theme, applicationName)
        false -> null
    }
}
