package action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import action.console.BashConsoleRunner

class KwaiRunIntermediatesAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        FileDocumentManager.getInstance().saveAllDocuments()
        BashConsoleRunner.start(anActionEvent, "kkr run --intermediates", "$baseShellCommand run --intermediates")
    }
}