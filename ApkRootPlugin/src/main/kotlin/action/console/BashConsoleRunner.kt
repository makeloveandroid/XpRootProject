package action.console

import com.esotericsoftware.minlog.Log
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.console.LanguageConsoleImpl
import com.intellij.execution.console.LanguageConsoleView
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import action.utils.BashInterpreterDetection

class BashConsoleRunner(
    project: Project,
    consoleTitle: String,
    val basePath: String,
    workingDir: String?,
    private val shellCommand: String?,
)
    : AbstractConsoleRunnerWithHistory<LanguageConsoleView>(project, consoleTitle, workingDir) {

    override fun createConsoleView(): LanguageConsoleView? {
        val consoleView = LanguageConsoleImpl(project, "Kwai", PlainTextFileType.INSTANCE.language)
        consoleView.consoleEditor.isRendererMode = true
        consoleView.consoleEditor.setCaretEnabled(false)
        return consoleView
    }

    @Throws(ExecutionException::class)
    override fun createProcess(): Process? {
        val bashLocation = BashInterpreterDetection.instance().findBestLocation()
                ?: throw ExecutionException("Could not locate the bash executable")
        val commandLine = GeneralCommandLine().withWorkDirectory(workingDir)
        commandLine.exePath = bashLocation
        commandLine.addParameters("-c")
        commandLine.addParameters(shellCommand)
        val process = commandLine.createProcess()
        processMap[basePath] = process
        return process
    }

    override fun createProcessHandler(process: Process): OSProcessHandler {
        return ColoredProcessHandler(process, null)
    }

    override fun createExecuteActionHandler(): ProcessBackedConsoleExecuteActionHandler {
        processHandlerMap[basePath] = processHandler
        return ProcessBackedConsoleExecuteActionHandler(processHandler, true)
    }

    companion object {
        var processHandlerMap: HashMap<String, ProcessHandler> = HashMap()
        var processMap: HashMap<String, Process> = HashMap()

        fun start(anActionEvent: AnActionEvent, consoleTitle: String, shellCommand: String?) {
            try {
                val project = anActionEvent.project
                val basePath = project!!.basePath
                if (basePath != null) {
                    BashConsoleRunner(project, consoleTitle, basePath, basePath, shellCommand).initAndRun()
                }
            } catch (ex: ExecutionException) {
                Log.error("Error running kwai remote", ex)
            }
        }

        fun start(anActionEvent: AnActionEvent, consoleTitle: String, workDir: String, shellCommand: String?) {
            try {
                val project = anActionEvent.project
                val basePath = project!!.basePath
                if (basePath != null) {
                    BashConsoleRunner(project, consoleTitle, basePath, workDir, shellCommand).initAndRun()
                }
            } catch (ex: ExecutionException) {
                Log.error("Error running kwai remote", ex)
            }
        }
    }
}