package action

import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiClearAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        BashConsoleRunner.start(anActionEvent, "kkr clear", "$baseShellCommand clear")
    }
}