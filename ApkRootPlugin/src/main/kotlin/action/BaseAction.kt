package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

abstract class BaseAction : AnAction() {
    val baseShellCommand = "kkr"
    override fun update(anActionEvent: AnActionEvent) {
        val process = BashConsoleRunner.processMap[anActionEvent.project?.basePath]
        if (process == null) {
            anActionEvent.presentation.isEnabled = anActionEvent.project != null
        } else {
            anActionEvent.presentation.isEnabled = !process.isAlive
        }
    }
}