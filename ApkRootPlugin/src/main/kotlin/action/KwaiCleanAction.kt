package action

import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiCleanAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        // 调试使用
//        BashConsoleRunner.start(anActionEvent, "kkr clean", "sleep 10m")

        BashConsoleRunner.start(anActionEvent, "kkr clean", "$baseShellCommand clean")
    }
}