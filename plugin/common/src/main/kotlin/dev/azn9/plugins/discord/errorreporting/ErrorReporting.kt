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
import dev.azn9.plugins.discord.utils.Plugin
import java.awt.Component
import java.io.PrintWriter
import java.io.StringWriter

fun gatherErrorData(event: IdeaLoggingEvent, additionalInfo: String?): MutableMap<String, String> {
    val appInfo = ApplicationInfoEx.getInstanceEx()
    val namesInfo = ApplicationNamesInfo.getInstance()
    val error = event.throwable

    val sw = StringWriter()
    error.printStackTrace(PrintWriter(sw))
    val stackTrace = sw.toString()

    return mutableMapOf(
        "Plugin Version" to Plugin.version.toString(),
        "OS Name" to SystemInfo.OS_NAME,
        "Java Version" to SystemInfo.JAVA_VERSION,
        "App Name" to namesInfo.productName,
        "App Full Name" to namesInfo.fullProductName,
        "App Version name" to appInfo.versionName,
        "Is EAP" to java.lang.Boolean.toString(appInfo.isEAP),
        "App Build" to appInfo.build.asString(),
        "App Version" to appInfo.fullVersion,

        "error.message" to (error.message ?: "/"),
        "error.stacktrace" to stackTrace,
        "error.hash" to error.stackTrace.contentHashCode().toString(),

        "additional.info" to (additionalInfo ?: "/")
    )
}

class SendErrorTask(
    private val project: Project?,
    private val event: IdeaLoggingEvent,
    private val additionalInfo: String?,
    private val parentComponent: Component,
    private val submittedReportConsumer: Consumer<in SubmittedReportInfo>
) : Task.Backgroundable(project, "Sending error report...") {

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true
        indicator.text = "Sending error report..."
        indicator.fraction = 0.0

        gatherErrorData(event, additionalInfo)

        ApplicationManager.getApplication().invokeLater {
            //val status = SubmittedReportInfo("", "", SubmittedReportInfo.SubmissionStatus.NEW_ISSUE)
            val status = SubmittedReportInfo(SubmittedReportInfo.SubmissionStatus.NEW_ISSUE)

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

        SendErrorTask(project, event, additionalInfo, parentComponent, consumer).queue()

        return true
    }

    override fun getReportActionText(): String {
        return "Report to Azn9"
    }

}
