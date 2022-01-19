package action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import action.console.BashConsoleRunner

class KwaiRunAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        FileDocumentManager.getInstance().saveAllDocuments()
        // 调试使用
//        BashConsoleRunner.start(anActionEvent, "kkr run", "sleep 10m")

        BashConsoleRunner.start(anActionEvent, "kkr run", "$baseShellCommand run")
    }
}