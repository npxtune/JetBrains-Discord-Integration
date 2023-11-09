/*
 * Copyright 2017-2020 Aljoscha Grebe
 * Copyright 2017-2020 Axel JOLY (Azn9) - https://github.com/Azn9
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

package com.almightyalpaca.jetbrains.plugins.discord.plugin.diagnose

import com.almightyalpaca.jetbrains.plugins.discord.plugin.DiscordPlugin
import com.almightyalpaca.jetbrains.plugins.discord.plugin.settings.values.ApplicationType
import com.intellij.ide.ApplicationInitializedListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Suppress("UnstableApiUsage")
class DiagnosePreloadingActivity : ApplicationInitializedListener {

    @Suppress("OVERRIDE_DEPRECATION")
    override fun componentsInitialized() {
        diagnose()
    }

    @Suppress("MissingRecentApi")
    suspend fun execute(asyncScope: CoroutineScope) {
        asyncScope.launch {
            diagnose()
        }
    }

    private fun diagnose() {
        DiscordPlugin.LOG.info("App starting, diagnosing environment")

        DiscordPlugin.LOG.info("Application identifiers: ${ApplicationType.IDE.applicationName}, ${ApplicationType.IDE_EDITION.applicationName}")

        diagnoseService.discord
        diagnoseService.plugins
        diagnoseService.ide
    }
}
