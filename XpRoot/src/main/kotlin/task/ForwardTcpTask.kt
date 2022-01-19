package task
import com.wind.meditor.utils.ShellCmdUtil
import util.Log
import util.TextUtils
import java.io.File

class ForwardTcpTask(val adbPath: File?) : Task<Unit, String>() {
    override fun execute(): String {
        val cmd = "adb forward tcp:8000 localabstract:app_hook"
        Log.d("ForwardTcpTask", "执行命令:$cmd")
        var s = ShellCmdUtil.execCmd(cmd, adbPath ?: null)
        if (TextUtils.isEmpty(s)) {
            s = "成功!"
        }
        Log.d("ForwardTcpTask", "执行命令完成:$s")
        return s
    }

    override fun complete(result: String) {

    }


}