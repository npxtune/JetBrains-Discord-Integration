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

package dev.azn9.plugins.discord.icons.source.classpath

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.icons.source.*
import kotlinx.coroutines.*
import org.apache.commons.io.FilenameUtils
import java.io.InputStream
import java.net.URI
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.coroutines.CoroutineContext
import kotlin.io.path.Path

class ClasspathSource(path: String) : Source, CoroutineScope {
    private val parentJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + parentJob

    private val basePath = "/$path"
    private val pathLanguages = "$basePath/languages"
    private val pathApplications = "$basePath/applications"
    val pathThemes = "$basePath/themes"

    override var languageMap: LanguageMap = readLanguages()
    override var themeMap: ThemeMap = readThemes()
    override var applicationMap: ApplicationMap = readApplications()

    init {
        if (languageMap.isEmpty()) {
            DiscordPlugin.LOG.error("No languages found!")
        }
        if (themeMap.isEmpty()) {
            DiscordPlugin.LOG.error("No themes found!")
        }
        if (applicationMap.isEmpty()) {
            DiscordPlugin.LOG.error("No applications found!")
        }
    }

    private fun readLanguages() = ClasspathLanguageSourceMap(this, read(pathLanguages, ::LanguageSource)).toLanguageMap()
    private fun readThemes() = ClasspathThemeSourceMap(this, read(pathThemes, ::ThemeSource)).toThemeMap()
    private fun readApplications() = ClasspathApplicationSourceMap(read(pathApplications, ::ApplicationSource)).toApplicationMap()

    private fun <T> read(path: String, factory: (String, JsonNode) -> T): Map<String, T> {
        val mapper = ObjectMapper(YAMLFactory())

        return listResources(path, ".yaml")
            .map { p ->
                try {
                    val node: JsonNode = mapper.readTree(loadResource(p))
                    val id = FilenameUtils.getBaseName(p)
                    id to factory(id, node)
                } catch (e: Exception) {
                    throw IllegalStateException("Error while generating $p")
                }
            }
            .toMap()
    }

    fun loadResource(location: String): InputStream? = ClasspathSource::class.java.getResourceAsStream(location)

    fun listResources(path: String, extension: String): Sequence<String> {
        return (loadResource("$path/index")
            ?.bufferedReader()
            ?.lineSequence()
            ?: throw IllegalStateException("could not find index for $path"))
            .filter { it.endsWith(extension) }
            .map { p -> "$path/$p" }
    }

    override fun getLanguagesOrNull(): LanguageMap = languageMap
    override fun getThemesOrNull(): ThemeMap = themeMap
    override fun getApplicationsOrNull(): ApplicationMap = applicationMap
}
