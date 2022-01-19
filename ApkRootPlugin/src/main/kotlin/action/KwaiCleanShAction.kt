package action

import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiCleanShAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        BashConsoleRunner.start(anActionEvent, "kkr sh clean.sh", "$baseShellCommand sh \"./clean.sh\"")
    }
}