package gui

import action.BaseAction
import action.console.BashConsoleRunner
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils


class ForwardTcpAction : BaseAction() {

    private var project: Project? = null

    override fun actionPerformed(event: AnActionEvent) {
        project = event.project
        if (project == null) {
            return
        }
//        val adb = AndroidSdkUtils.getAdb(project)
        BashConsoleRunner.start(event,
            "ζζεΊη¨",
            "adb forward tcp:8000 localabstract:app_hook")
    }
}