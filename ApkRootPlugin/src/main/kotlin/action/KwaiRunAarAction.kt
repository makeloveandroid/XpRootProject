package action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import action.console.BashConsoleRunner

class KwaiRunAarAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        FileDocumentManager.getInstance().saveAllDocuments()
        BashConsoleRunner.start(anActionEvent, "kkr aarrun", "$baseShellCommand run -s")
    }
}