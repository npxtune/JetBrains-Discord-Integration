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

package dev.azn9.plugins.discord.time

import com.intellij.ide.IdeEventQueue
import com.intellij.ide.IdleTracker
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.AccessToken
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.UserDataHolder
import com.intellij.openapi.vfs.VirtualFile
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.render.renderService
import dev.azn9.plugins.discord.settings.settings
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import kotlin.math.min

@Service
class TimeService : TimeServiceAbstract, Disposable {

    private lateinit var listenerToken: AccessToken
    private val loaded = AtomicBoolean(false)

    override val idle
        get() = idleSince.get() != null

    /**
     * Timestamp of when the IDE went into idle mode.
     * Null means the IDE is currently active.
     */
    private val idleSince = AtomicReference<Long?>(null)

    private val idleListener = Runnable {
        idleSince.updateAndGet { old ->
            when (old) {
                null -> System.currentTimeMillis()
                else -> min(old, System.currentTimeMillis())
            }
        }

        renderService.render(true)
    }

    /**
     * This listener is called whenever the user interacts with the IDE.
     * **This is called very often, don't do too much stuff in here.**
     * Especially not outside the `if` statement.
     */
    private val activityListener = Runnable {
        val idleSince = idleSince.getAndSet(null)

        if (idleSince != null) {
            val idleDuration = System.currentTimeMillis() - idleSince

            if (settings.timeoutResetTimeEnabled.getStoredValue()) {
                val now = System.currentTimeMillis()
                val application = ApplicationManager.getApplication()
                application.durationIdle = 0
                application.timeOpened = now

                for (project in ProjectManager.getInstance().openProjects) {
                    project.durationIdle = 0
                    project.timeOpened = now

                    for (file in FileEditorManager.getInstance(project).openFiles) {
                        file.durationIdle = 0
                        file.timeOpened = now
                    }
                }
            } else {
                ApplicationManager.getApplication().durationIdle += idleDuration

                for (project in ProjectManager.getInstance().openProjects) {
                    project.durationIdle += idleDuration

                    for (file in FileEditorManager.getInstance(project).openFiles) {
                        file.durationIdle += idleDuration
                    }
                }
            }

            renderService.render(true)
        }
    }

    init {
        IdeEventQueue.getInstance().doWhenReady {
            IdeEventQueue.getInstance().addActivityListener(activityListener, this)
        }
    }

    override fun load() {
        DiscordPlugin.LOG.debug("Loading TimeService vPOST-231")

        if (loaded.getAndSet(true)) {
            unload()
        }

        initializeApplication(ApplicationManager.getApplication())

        for (project in ProjectManager.getInstance().openProjects) {
            initializeProject(project)

            for (file in FileEditorManager.getInstance(project).openFiles) {
                initializeFile(file)
            }
        }

        val timeoutMillis = settings.timeoutMinutes.getStoredValue() * 60 * 1000

        listenerToken = IdleTracker.getInstance().addIdleListener(timeoutMillis, idleListener)
    }

    private fun unload() {
        if (loaded.getAndSet(false) && ::listenerToken.isInitialized) {
            listenerToken.finish()
            listenerToken = AccessToken.EMPTY_ACCESS_TOKEN
        }
    }

    override fun dispose() {
        unload()
    }

    override fun initializeApplication(application: Application) = initialize(application)
    override fun initializeProject(project: Project) = initialize(project)
    override fun initializeFile(file: VirtualFile) = initialize(file)

    private fun initialize(holder: UserDataHolder) {
        if (holder.getUserData(TIME_OPENED) == null) {
            holder.putUserData(TIME_OPENED, System.currentTimeMillis())
        }
        if (holder.getUserData(TIME_IDLE) == null) {
            holder.putUserData(TIME_IDLE, 0)
        }
    }
}
