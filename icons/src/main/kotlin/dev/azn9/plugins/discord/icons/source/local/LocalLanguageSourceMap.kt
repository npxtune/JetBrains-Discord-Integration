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

package dev.azn9.plugins.discord.icons.source.local

import dev.azn9.plugins.discord.icons.matcher.Matcher
import dev.azn9.plugins.discord.icons.source.Language
import dev.azn9.plugins.discord.icons.source.LanguageMap
import dev.azn9.plugins.discord.icons.source.LanguageSource
import dev.azn9.plugins.discord.icons.source.abstract.AbstractLanguageSourceMap

class LocalLanguageSourceMap(private val source: LocalSource, map: Map<String, LanguageSource>) : AbstractLanguageSourceMap(map) {
    override fun createLanguageMap(languages: Map<String, Language>): LanguageMap = LocalLanguageMap(languages.values)

    override fun createDefaultLanguage(name: String, assetId: String): Language.Default = LocalLanguage.Default(source, name, assetId)

    override fun createSimpleLanguage(fileId: String, name: String, parent: Language?, assetIds: List<String>?, matchers: Map<dev.azn9.plugins.discord.icons.matcher.Matcher.Target, dev.azn9.plugins.discord.icons.matcher.Matcher>): Language.Simple =
        LocalLanguage.Simple(source, fileId, name, parent, assetIds, matchers)
}
