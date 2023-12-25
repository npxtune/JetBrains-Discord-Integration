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

package dev.azn9.plugins.discord.postLoad.post231

import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.notifications.ApplicationUpdateNotification
import dev.azn9.plugins.discord.notifications.ProjectShowNotification
import dev.azn9.plugins.discord.render.renderService
import dev.azn9.plugins.discord.settings.settings
import dev.azn9.plugins.discord.settings.values.ProjectShow
import dev.azn9.plugins.discord.utils.DisposableCoroutineScope
import dev.azn9.plugins.discord.utils.Plugin
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class NotificationPostStartupActivity : ProjectActivity, DisposableCoroutineScope {
    override val parentJob: Job = SupervisorJob()

    override suspend fun execute(project: Project) {
        launch {
            checkUpdate()
            checkAskShowProject(project)
        }
    }

    private fun checkUpdate() {
        DiscordPlugin.LOG.info("Checking for plugin update")

        val version = Plugin.version
        if (version != null && version.toString() != settings.applicationLastUpdateNotification.getStoredValue() && version.isStable()) {
            DiscordPlugin.LOG.info("Plugin update found, showing changelog")

            settings.applicationLastUpdateNotification.setStoredValue(version.toString())

            launch { ApplicationUpdateNotification.show(version.toString()) }
        }
    }

    private suspend fun checkAskShowProject(project: Project) {
        DiscordPlugin.LOG.info("Checking for project confirmation")

        val settings = project.settings

        if (settings.show.getStoredValue() == ProjectShow.ASK) {
            DiscordPlugin.LOG.info("Showing project confirmation dialog")

            val result = ProjectShowNotification.show(project)

            DiscordPlugin.LOG.info("Project confirmation result=$result")

            settings.show.setStoredValue(result)
            renderService.render()
        }
    }
}
