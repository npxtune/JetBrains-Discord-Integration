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

package dev.azn9.plugins.discord.icons.source.abstract

import dev.azn9.plugins.discord.icons.matcher.Matcher
import dev.azn9.plugins.discord.icons.source.Language
import dev.azn9.plugins.discord.icons.source.LanguageMatch
import dev.azn9.plugins.discord.icons.utils.concat

sealed class AbstractLanguage(final override val id: String, final override val name: String) : Language {
    override fun toString(): String {
        return "AbstractLanguage(id='$id', name='$name')"
    }

    abstract class Simple(id: String, name: String, final override val parent: Language?, assetIds: List<String>?, final override val matchers: Map<dev.azn9.plugins.discord.icons.matcher.Matcher.Target, dev.azn9.plugins.discord.icons.matcher.Matcher>) :
        AbstractLanguage(id, name), Language.Simple {

        final override val assetIds: Iterable<String> = concat(assetIds, parent?.assetIds)
    }

    abstract class Default(name: String, final override val assetId: String) : AbstractLanguage("default", name), Language.Default {
        final override val assetIds: Iterable<String> = listOf(assetId)
        final override val parent: Language? = null
        final override val matchers: Map<dev.azn9.plugins.discord.icons.matcher.Matcher.Target, dev.azn9.plugins.discord.icons.matcher.Matcher> get() = emptyMap()
        final override fun findMatch(target: dev.azn9.plugins.discord.icons.matcher.Matcher.Target, fields: Collection<String>): LanguageMatch? = null
    }
}
