package task

import com.wind.meditor.utils.ShellCmdUtil
import util.Log
import wind.android.content.res.ManifestParser
import java.io.File

open class StartActivityTask(val manifestData: ManifestParser.ManifestData, val adbPath: File?) : Task<File, String>() {
    override fun execute(): String {
        Log.d("StartActivityTask", "启动 [${manifestData.currentActivity}]")
        val installApp = "adb shell am start -n ${manifestData.packageName}/${manifestData.currentActivity}"
        return ShellCmdUtil.execCmd(installApp, adbPath)
    }

    override fun complete(result: String) {
        Log.d("StartActivityTask", "HomeActivity 启动完成!")
    }
}