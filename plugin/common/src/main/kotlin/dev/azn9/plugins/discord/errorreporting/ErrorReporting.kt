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

package dev.azn9.plugins.discord.errorreporting

import com.google.gson.Gson
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ApplicationNamesInfo
import com.intellij.openapi.application.ex.ApplicationInfoEx
import com.intellij.openapi.diagnostic.ErrorReportSubmitter
import com.intellij.openapi.diagnostic.IdeaLoggingEvent
import com.intellij.openapi.diagnostic.SubmittedReportInfo
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.Consumer
import dev.azn9.plugins.discord.DiscordPlugin
import dev.azn9.plugins.discord.utils.Plugin
import dev.azn9.plugins.discord.utils.infoLazy
import dev.azn9.plugins.discord.utils.warnLazy
import org.kohsuke.github.GHIssue
import org.kohsuke.github.GHIssueState
import org.kohsuke.github.GHRepository
import org.kohsuke.github.GitHubBuilder
import java.awt.Component
import java.io.PrintWriter
import java.io.StringWriter
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class ErrorData(
    val pluginVersion: String,
    val osName: String,
    val javaVersion: String,
    val appFullName: String,
    val appBuild: String,
    val appVersion: String,
    val errorMessage: String,
    val stackTrace: String,
    val hash: String,
    val additionalInfo: String
)

fun gatherErrorData(event: IdeaLoggingEvent, additionalInfo: String?): ErrorData {
    val appInfo = ApplicationInfoEx.getInstanceEx()
    val namesInfo = ApplicationNamesInfo.getInstance()
    val error = event.throwable

    val sw = StringWriter()
    error.printStackTrace(PrintWriter(sw))
    val stackTrace = sw.toString()

    return ErrorData(
        Plugin.version.toString(),
        SystemInfo.OS_NAME,
        SystemInfo.JAVA_VERSION,
        namesInfo.fullProductName,
        appInfo.build.asString(),
        appInfo.fullVersion,
        error.message ?: "?",
        stackTrace,
        error.stackTrace.contentHashCode().toString(),
        additionalInfo ?: "/"
    )
}

fun sendErrorReport(data: ErrorData): SubmittedReportInfo {
    val gson = Gson()
    val httpClient = HttpClient.newBuilder().build()
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://jdierrors.azn9.dev/"))
        .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(data)))
        .build()
    val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

    if (response.statusCode() != 200) {
        DiscordPlugin.LOG.warnLazy { "Failed to send error report" }
        return SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.FAILED)
    }

    DiscordPlugin.LOG.infoLazy { "Error report sent" }
    return SubmittedReportInfo(null, "Error report sent", SubmittedReportInfo.SubmissionStatus.NEW_ISSUE)
}

class SendErrorTask(
    project: Project?,
    private val event: IdeaLoggingEvent,
    private val additionalInfo: String?,
    private val submittedReportConsumer: Consumer<in SubmittedReportInfo>
) : Task.Backgroundable(project, "Sending error report...") {

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true
        indicator.text = "Sending error report..."
        indicator.fraction = 0.0

        val status = try {
            val data = gatherErrorData(event, additionalInfo)

            sendErrorReport(data)
        } catch (e: Exception) {
            DiscordPlugin.LOG.warnLazy(e) { "Failed to send error report" }

            SubmittedReportInfo(null, "Failed to send error report", SubmittedReportInfo.SubmissionStatus.FAILED)
        }

        ApplicationManager.getApplication().invokeLater {
            submittedReportConsumer.consume(status)
        }
    }
}

class ErrorReporting : ErrorReportSubmitter() {

    override fun submit(events: Array<out IdeaLoggingEvent>, additionalInfo: String?, parentComponent: Component, consumer: Consumer<in SubmittedReportInfo>): Boolean {
        val mgr = DataManager.getInstance()
        val context = mgr.getDataContext(parentComponent)
        val project: Project? = CommonDataKeys.PROJECT.getData(context)

        val event = events.firstOrNull { it.throwable != null } ?: return false

        SendErrorTask(project, event, additionalInfo, consumer).queue()

        return true
    }

    override fun getReportActionText(): String {
        return "Report to Azn9"
    }

}
