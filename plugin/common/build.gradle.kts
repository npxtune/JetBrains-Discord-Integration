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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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

fun properties(key: String) = providers.gradleProperty(key)
val isCI by lazy { System.getenv("CI") != null }

plugins {
    alias(libs.plugins.kotlin.common)
    alias(libs.plugins.intellij.common)

    antlr
}

// Used only to add the required dependencies
intellij {
    pluginName.set(properties("pluginName").get())
    version(libs.versions.ide.v212) // Lowest supported version
    downloadSources(!isCI)
    instrumentCode(false)
    updateSinceUntilBuild(false)
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")

    mavenLocal()
    maven("https://nexus.azn9.dev/repository/public")

    maven("https://www.jetbrains.com/intellij-repository/releases/")
}

dependencies {
    implementation(project(path = ":icons", configuration = "minimizedJar"))

    implementation(libs.kotlinx.serialization)
    implementation(libs.discord.ipc)
    implementation(libs.commons.io)
    implementation(libs.jackson.dataformat.yaml)

    antlr(libs.antlr)
    implementation(libs.antlr.runtime)
}

val generatedSourceDir = project.file("src/generated")
val generatedJavaSourceDir = generatedSourceDir.resolve("java")

sourceSets {
    main {
        java {
            srcDir(generatedJavaSourceDir)
        }
    }
}

kotlin {
    jvmToolchain {
        //vendor = JvmVendorSpec.JETBRAINS
        languageVersion.set(JavaLanguageVersion.of("11"))
    }
}

configurations {
    // https://github.com/gradle/gradle/issues/820
    api {
        setExtendsFrom(extendsFrom.filter { it != antlr.get() })
    }

    implementation {
        exclude("org.jetbrains.kotlin", "kotlin-reflect")
        exclude("org.jetbrains.kotlin", "kotlin-stdlib")
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-common")
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk7")
        exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8")
        exclude("org.jetbrains.kotlin", "kotlin-test")
        exclude("org.jetbrains.kotlin", "kotlin-test-common")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-core-common")
        exclude("org.jetbrains.kotlinx", "kotlinx-coroutines-jdk8")
        exclude("org.slf4j", "slf4j-api")
    }
}

testing {
    @Suppress("UnstableApiUsage")
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter(libs.versions.junit.jupiter)

            targets.configureEach {
                testTask {
                    enableAssertions = true

                    maxHeapSize = "1G"
                }
            }

            dependencies {
                implementation(libs.kotlin.test)
            }
            project.tasks {
                compileTestKotlin {
                    dependsOn(generateTestGrammarSource)
                }
            }
        }
    }
}

tasks {
    generateGrammarSource {
        val packageName = "dev.azn9.plugins.discord.render.templates.antlr"

        arguments = arguments + listOf("-package", packageName, "-no-listener")
        outputDirectory = generatedJavaSourceDir.resolve(packageName.replace('.', File.separatorChar))
    }

    compileKotlin {
        dependsOn(generateGrammarSource)
    }

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
            freeCompilerArgs += "-Xjvm-default=all"
        }
    }
}
