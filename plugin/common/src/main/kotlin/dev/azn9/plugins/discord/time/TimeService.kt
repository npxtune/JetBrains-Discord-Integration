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

package dev.azn9.plugins.discord.time

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.Application
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile

val TIME_OPENED = Key.create<Long>("discord.time.opened")
val TIME_IDLE = Key.create<Long>("discord.time.idle")

/**
 * Timestamp of when the application was opened
 */
var Application.timeOpened
    get() = getUserData(TIME_OPENED) ?: System.currentTimeMillis()
    set(value) = putUserData(TIME_OPENED, value)

/**
 * Timestamp of when the project was opened
 */
var Project.timeOpened
    get() = getUserData(TIME_OPENED) ?: System.currentTimeMillis()
    set(value) = putUserData(TIME_OPENED, value)

/**
 * Timestamp of when the file was opened
 */
var VirtualFile.timeOpened
    get() = getUserData(TIME_OPENED) ?: System.currentTimeMillis()
    set(value) = putUserData(TIME_OPENED, value)

var Application.durationIdle
    get() = getUserData(TIME_IDLE) ?: 0
    set(value) = putUserData(TIME_IDLE, value)

var Project.durationIdle
    get() = getUserData(TIME_IDLE) ?: 0
    set(value) = putUserData(TIME_IDLE, value)

var VirtualFile.durationIdle
    get() = getUserData(TIME_IDLE) ?: 0
    set(value) = putUserData(TIME_IDLE, value)

/**
 * Calculated timestamp of the start of the active time of the application.
 * Another way to express this would be `now() - activeDuration`
 */
val Application.timeActive
    get() = timeOpened + durationIdle

/**
 * Calculated timestamp of the start of the active time of the project.
 * Another way to express this would be `now() - activeDuration`
 */
val Project.timeActive
    get() = timeOpened + durationIdle

/**
 * Calculated timestamp of the start of the active time of the file.
 * Another way to express this would be `now() - activeDuration`
 */
val VirtualFile.timeActive
    get() = timeOpened + durationIdle

@Suppress("RetrievingService")
val timeService: TimeServiceAbstract
    get() {
        val clazz = Class.forName("dev.azn9.plugins.discord.time.TimeService")
        return ApplicationManager.getApplication().getService(clazz) as TimeServiceAbstract
    }

interface TimeServiceAbstract {

    fun initializeApplication(application: Application)
    fun initializeProject(project: Project)
    fun initializeFile(file: VirtualFile)

    val idle: Boolean
    fun load()

}
