package gui

import action.BaseAction
import action.console.BashConsoleRunner
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.Messages
import org.jetbrains.android.sdk.AndroidSdkUtils
import org.jetbrains.android.util.AndroidUtils
import java.io.File


class VirusAppAction : BaseAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT) ?: return
        val apk = File(
            project.basePath,
            "/ks-applications/kwai-android/build/outputs/apk/dev/debug/kwai-android-dev-debug.apk"
        )
        if (!apk.exists()) {
            Messages.showMessageDialog(project, "未找到快手Apk，选去 kkr aarrun/run 一次吧!", "提示", Messages.getInformationIcon())
            return
        }
        val file = copyCoreJar(project)
        val virus = copyVirus(project)
//        val projectSdk = ProjectRootManager.getInstance(project).projectSdk;
//        val workDir = projectSdk?.homePath?.let {
//            "${it}/bin"
//        }
        val adb = AndroidSdkUtils.getAdb(project)
        AndroidUtils.getAndroidModule(project)
        val cmd = "java -jar ${file.absolutePath} ${apk.absolutePath} ${virus.absolutePath} ${adb?.parent ?: "路径没有"}"
        FileDocumentManager.getInstance().saveAllDocuments()
        BashConsoleRunner.start(event,
            "感染应用",
            cmd)
    }

    private fun copyVirus(project: Project): File {
        val parent = File("${project.basePath}/ks-applications/kwai-android/build/")
        parent.mkdirs()
        val file = File(parent, "virus.apk")
        if (!file.exists() || !file.isFile) {
            val resourceAsStream = FileUtil.getResourceAsStream("virus.apk")
            file.writeBytes(resourceAsStream.readAllBytes())
            resourceAsStream.close()
        }
        return file
    }

    private fun copyCoreJar(project: Project): File {
        val parent = File("${project.basePath}/ks-applications/kwai-android/build/")
        parent.mkdirs()
        val file = File(parent, "ApkRoot.jar")
        if (!file.exists() || !file.isFile) {
            val resourceAsStream = FileUtil.getResourceAsStream("ApkRoot.jar")
            file.writeBytes(resourceAsStream.readAllBytes())
            resourceAsStream.close()
        }
        return file
    }
}