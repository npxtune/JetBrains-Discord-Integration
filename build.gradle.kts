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

@file:Suppress("SuspiciousCollectionReassignment")

import com.github.benmanes.gradle.versions.updates.gradle.GradleReleaseChannel
import com.palantir.gradle.gitversion.VersionDetails
import groovy.lang.Closure
import java.nio.file.Files
import java.util.*

plugins {
    //alias(libs.plugins.kotlin.latest) apply false
    alias(libs.plugins.versions)
    alias(libs.plugins.gitversion)

    id("buildUtils")
}

group = "dev.azn9.jetbrains.plugins.discord"

val versionDetails: Closure<VersionDetails> by extra

var version = versionDetails().lastTag.removePrefix("v")
version += when (versionDetails().commitDistance) {
    0 -> ""
    else -> "+${versionDetails().commitDistance}"
}

project.version = version

allprojects {
    fun secret(name: String) {
        project.extra[name] = System.getenv(name) ?: return
    }

    secret("DISCORD_TOKEN")
    secret("BINTRAY_KEY")
    secret("JETBRAINS_TOKEN")
}

subprojects {
    group = rootProject.group.toString() + "." + project.name.lowercase(Locale.ROOT)
    version = rootProject.version

    val secrets: File = rootProject.file("secrets.gradle.kts")
    if (secrets.exists()) {
        apply(from = secrets)
    }

    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}

defaultTasks = mutableListOf("default")

tasks {
    dependencyUpdates {
        gradleReleaseChannel = GradleReleaseChannel.CURRENT.toString()

        rejectVersionIf {
            val preview = Regex("""[+_.-]?(alpha|beta|rc|cr|m|preview|eap|pr|M)[.\d-_]*$""", RegexOption.IGNORE_CASE)
                .containsMatchIn(candidate.version)

            val wrongKotlinVersion = candidate.group.startsWith("org.jetbrains.kotlin") && candidate.version != currentVersion

            return@rejectVersionIf preview || wrongKotlinVersion
        }
    }

    create<Delete>("clean") {
        group = "build"

        val regex = Regex("""JetBrains-Discord-Integration-\d+.\d+.\d+(?:\+\d+)?.zip""")

        Files.newDirectoryStream(project.projectDir.toPath())
            .filter { p -> regex.matches(p.fileName.toString()) }
            .forEach { p -> delete(p) }

        delete(project.layout.buildDirectory)
    }

    create("default") {
        val buildPlugin = project.tasks.getByPath("plugin:buildPlugin")
        dependsOn(buildPlugin)
    }

    create<Delete>("clean-sandbox") {
        group = "build"

        delete(project.file(".sandbox"))
    }
}

