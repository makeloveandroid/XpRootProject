package action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiStopAction : AnAction() {
    override fun update(anActionEvent: AnActionEvent) {
        val process = BashConsoleRunner.processMap[anActionEvent.project?.basePath]
        if (process != null) {
            anActionEvent.presentation.isEnabled = process.isAlive
        } else {
            anActionEvent.presentation.isEnabled = false
        }
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        BashConsoleRunner.processHandlerMap[anActionEvent.project?.basePath]?.destroyProcess()
    }
}