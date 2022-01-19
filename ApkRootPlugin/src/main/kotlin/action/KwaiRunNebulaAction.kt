package action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileDocumentManager
import action.console.BashConsoleRunner

class KwaiRunNebulaAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        FileDocumentManager.getInstance().saveAllDocuments()
        BashConsoleRunner.start(anActionEvent, "kkr run -DisNebula=true", "$baseShellCommand run")
    }
}