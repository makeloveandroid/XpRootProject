package task

import com.wind.meditor.utils.ShellCmdUtil
import util.Log
import java.io.File

open class InstallApkTask(val apkFile: File, val adbPath: File?) : Task<File, String>() {
    override fun execute(): String {
        Log.d("InstallApkTask", "开始安装Apk:${apkFile.absoluteFile}")

        val installApp = "adb install -r ${apkFile.absoluteFile}"
        return ShellCmdUtil.execCmd(installApp, adbPath)
    }

    override fun complete(result: String) {
        Log.d("InstallApkTask", "Apk安装结果: ${result.replace("\n", " ")}")
    }
}