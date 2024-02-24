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

import dev.azn9.plugins.discord.icons.matcher.Matcher
import dev.azn9.plugins.discord.icons.source.Language
import dev.azn9.plugins.discord.icons.source.LanguageMatch
import dev.azn9.plugins.discord.icons.source.abstract.AbstractLanguage

object ClasspathLanguage {
    class Simple(source: ClasspathSource, id: String, name: String, parent: Language?, assetIds: List<String>?, matchers: Map<dev.azn9.plugins.discord.icons.matcher.Matcher.Target, dev.azn9.plugins.discord.icons.matcher.Matcher>) :
        AbstractLanguage.Simple(id, name, parent, assetIds, matchers) {
        override val match: LanguageMatch = ClasspathLanguageMatch(source, name, this.assetIds)
    }

    class Default(source: ClasspathSource, name: String, assetId: String) : AbstractLanguage.Default(name, assetId) {
        override val match: LanguageMatch = ClasspathLanguageMatch(source, name, assetIds)
    }
}
