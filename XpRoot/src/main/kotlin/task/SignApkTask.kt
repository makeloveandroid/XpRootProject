package task

import com.android.apksigner.ApkSignerTool
import com.wind.meditor.utils.ShellCmdUtil
import util.Log
import java.io.File
import java.lang.RuntimeException
import java.util.*

class SignApkTask(val unsignApk: File) : Task<File, File>() {
    override fun execute(): File {
        Log.d("SignApkTask", "开始重新签名App!!!")
        // copy 签名
        val singFile = File(unsignApk.parent, "keystore").apply {
            writeBytes(Thread.currentThread().contextClassLoader.getResourceAsStream("keystore").readBytes())
        }

        val signApk = File(unsignApk.parent, "app-signed.apk")
        signApk(
            unsignApk.absolutePath,
            singFile.absolutePath, signApk.absolutePath
        )
        return signApk
    }

    // 使用Android build-tools里自带的apksigner工具进行签名
    private fun signApkUsingAndroidApksigner(
        apkPath: String,
        keyStorePath: String,
        signedApkPath: String,
        keyStorePassword: String
    ): Boolean {
        val commandList = ArrayList<String>()
        commandList.add("sign")
        commandList.add("--ks")
        commandList.add(keyStorePath)
        commandList.add("--ks-key-alias")
        commandList.add("caohe.keystore")
        commandList.add("--ks-pass")
        commandList.add("pass:$keyStorePassword")
        commandList.add("--key-pass")
        commandList.add("pass:$keyStorePassword")
        commandList.add("--out")
        commandList.add(signedApkPath)
        commandList.add("--v1-signing-enabled")
        commandList.add("true")
        commandList.add("--v2-signing-enabled") // v2签名不兼容android 6
        commandList.add("true")
        commandList.add("--v3-signing-enabled") // v3签名不兼容android 6
        commandList.add("false")
        commandList.add(apkPath)
        val size = commandList.size
        var commandArray: Array<String?>? = arrayOfNulls(size)
        commandArray = commandList.toArray(commandArray)
        try {
            ApkSignerTool.main(commandArray)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    private fun signApk(
        apkPath: String,
        keyStorePath: String,
        signedApkPath: String
    ): Boolean {
        if (signApkUsingAndroidApksigner(apkPath, keyStorePath, signedApkPath, "111111")) {
            return true
        }
        throw RuntimeException("Apk 签名失败!!")
    }

    override fun complete(result: File) {
        Log.d("SignApkTask", "APP 签名完成 ${result.absolutePath}")
    }

}