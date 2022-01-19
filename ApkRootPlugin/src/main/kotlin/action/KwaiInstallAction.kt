package action

import com.intellij.openapi.actionSystem.AnActionEvent
import action.console.BashConsoleRunner

class KwaiInstallAction : BaseAction() {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val installPath = anActionEvent.project?.basePath + "/ks-applications/kwai-android/build/outputs/apk/dev/debug/kwai-android-dev-debug.apk"
        BashConsoleRunner.start(anActionEvent, "adb install apk",
                "echo \"正在安装" + installPath + "\nwaiting...\"" +
                        " && adb install -r " + installPath)
    }
}