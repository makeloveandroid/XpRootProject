package task

import org.apache.commons.cli.CommandLine
import java.io.File

object TaskManager {
    fun call(commandLine: CommandLine) {
        val host = commandLine.getOptionValue("host")
        val virus = commandLine.getOptionValue("virus")
        val debug = commandLine.getOptionValue("debug")
        val dex = commandLine.getOptionValue("dex")
        val install = commandLine.getOptionValue("install")
        val isTrace = commandLine.getOptionValue("isTrace") == "1"
        val adbPath = null
        ForwardTcpTask(adbPath).call()
        // 第一步 Copy 1份 Apk
        val result = UnZipApkTask(host).call()
        val unApkFile = result.file
        val uncompressedFilesOrExts = result.uncompressedFilesOrExts

        // 保存原来的签名文件
//        SaveOriginSignTask(File(host), unApkFile).call()

        // 删除以前的签名信息
        DeleteMetaInfoTask(unApkFile).call()
        val manifestData = if (!isTrace) {
            // 追加dex
            val appendDexFile = CopyAppendDexTask(unApkFile).call()

            // 第二部获取 application
            val manifestData = GetApplicationTask(unApkFile).call()
            val applicationName = manifestData.applicationName
            // 判断是否要替换或增加 application
            var newApplicationName = ""
            if (dex != "1") {
                newApplicationName = if (applicationName.isEmpty()) {
                    AndroidManifestEditorTask.PROXY_APPLICATION_NAME
                } else {
                    applicationName
                }
            }

            if (newApplicationName.isNotEmpty() || debug == "1") {
                // 修改 Manifest debug 参数 或者 application 参数
                AndroidManifestEditorTask(debug == "1", newApplicationName, unApkFile).call()
            }

            // 注入hook start方法
            if (applicationName.isNotEmpty()) {
                if (dex == "1") {
                    // dex 1 的模式通过修改原有的 application 注入 (可能存在dex方法数超655535)
                    ModifyApplicationDexTask(unApkFile, appendDexFile, applicationName).call()
                } else {
                    // 默认模式通过 替换 继承 Application 方式 (规避掉方法数超 655535) appendDexFile 就是替换的application地址
                    ModifyNewApplicationDexTask(unApkFile, appendDexFile, applicationName).call()
                }
            }
            // copy 文件
            CopyXpCoreTask(unApkFile).call()
            // copy xposed 模块
            CopyXpModelTask(unApkFile, mutableListOf(virus)).call()
            manifestData
        } else {
            // 第二部获取 application
            val manifestData = GetApplicationTask(unApkFile).call()
            // 开启debug模式
            AndroidManifestEditorTask(true, "", unApkFile).call()
            ModifyDexInjectMethodTask(unApkFile, virus).call()
            manifestData
        }


        // 重新压缩App
        val unsignedApp = ZipTask(unApkFile, uncompressedFilesOrExts).call()
        // 重新签名
        val signApkFile = SignApkTask(unsignedApp).call()
        if (install == "1") {
            InstallApkTask(signApkFile, adbPath).call()
            StartActivityTask(manifestData, adbPath).call()
        }
    }
}