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

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFocusManager
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.icons.source.ASSET_URL
import dev.azn9.plugins.discord.icons.source.Theme
import dev.azn9.plugins.discord.icons.source.abstract.AbstractAsset
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Base64
import javax.imageio.ImageIO

class ClasspathAsset(private val source: ClasspathSource, id: String, theme: Theme, private val applicationName: String) : AbstractAsset(id, theme) {
    override fun getImage(size: Int?): BufferedImage? = when (id) {
        "project" -> {
            val project: Project? = IdeFocusManager.getGlobalInstance().lastFocusedFrame?.project

            if (project == null) {
                DiscordPlugin.LOG.warn("No project found")
            }

            project?.basePath?.let {
                DiscordPlugin.LOG.warn("Project base path: $it")
                File("$it/.idea/icon.png").takeIf(File::exists)?.inputStream()
            } ?: source.loadResource("${source.pathThemes}/${theme.id}/applications/$applicationName.png")
        }
        "application" -> source.loadResource("${source.pathThemes}/${theme.id}/applications/$applicationName.png")
        else -> source.loadResource("${source.pathThemes}/${theme.id}/languages/$id.png")
    }.use(ImageIO::read)

    override fun getUrl(): String {
        return when (id) {
            "project" -> {
                val project: Project? = IdeFocusManager.getGlobalInstance().lastFocusedFrame?.project

                project?.basePath?.let {
                    val inputStream = File("$it/.idea/icon.png").takeIf(File::exists)?.inputStream()

                    inputStream?.let {
                        // Convert to a base64 string
                        val os = ByteArrayOutputStream(inputStream.available())
                        val image: BufferedImage = ImageIO.read(inputStream)
                        ImageIO.write(image, "png", os)
                        val bytes: ByteArray = os.toByteArray()

                        val base64String: String = Base64.getEncoder().encodeToString(bytes)

                        "data:image/png;base64,$base64String"
                    }
                } ?: "${ASSET_URL}/themes/${theme.id}/applications/$applicationName.png"
            }
            "application" -> "${ASSET_URL}/themes/${theme.id}/applications/$applicationName.png"
            else -> "${ASSET_URL}/themes/${theme.id}/languages/$id.png"
        }
    }
}
