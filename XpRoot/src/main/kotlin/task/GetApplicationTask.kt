package task

import util.Log
import wind.android.content.res.ManifestParser
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

open class GetApplicationTask(val apkFile: File) : Task<File, ManifestParser.ManifestData>() {
    companion object {
        const val ANDROID_MANIFEST = "AndroidManifest.xml"
    }

    override fun execute(): ManifestParser.ManifestData {
        val manifestInput = File(apkFile, ANDROID_MANIFEST).inputStream()
        return ManifestParser.parseManifestFile(manifestInput)
    }

    override fun complete(result: ManifestParser.ManifestData) {
        Log.d("GetApplicationTask", "获取到${result.packageName} application ${result.applicationName}")
    }

}