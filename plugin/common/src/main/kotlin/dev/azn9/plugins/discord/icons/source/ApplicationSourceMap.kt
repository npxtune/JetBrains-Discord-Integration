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

package dev.azn9.plugins.discord.icons.source

import dev.azn9.plugins.discord.DiscordPlugin
import kotlin.streams.asSequence

interface ApplicationSourceMap : Map<String, ApplicationSource> {
    fun createApplicationMap(applications: Map<String, Application>): ApplicationMap
    fun createApplication(id: String, discordId: Long, dummyFile: String): Application

    fun toApplicationMap(): ApplicationMap {
        val applications = values.stream()
            .map { it.asApplications() }
            .asSequence()
            .flatMap { it.entries }
            .associate { it.key to it.value }

        DiscordPlugin.LOG.warn("Applications: $applications")

        return createApplicationMap(applications)
    }

    fun ApplicationSource.asApplications(): Map<String, Application> {
        val discordId: Long = node["discordId"]?.asLong()!!
        val dummyFile: String = node["dummyFile"]?.textValue()!!

        return node["ids"].map {
            createApplication(it.textValue(), discordId, dummyFile)
        }.associateBy { it.id }
    }
}
