package action

import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiSyncAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        BashConsoleRunner.start(anActionEvent, "kkr sync", "$baseShellCommand sync")
    }
}