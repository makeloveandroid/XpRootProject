package com.ks.core

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.blankj.utilcode.util.Utils
import com.ks.core.action.MethodHook
import com.ks.core.callaction.CallActionManager
import com.ks.core.net.data.WebSocketManager
import com.ks.core.util.LocationHookUtils
import com.ks.core.util.SharedPreferencesUtils
import java.io.File

class HookReceiver(val app: Application) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        val pkg = intent.getStringExtra("HOOK_PKG")
        log("收到广播:${pkg}")
        if (pkg == app.packageName) {
            log("开始hook:${pkg}")
            Core.appHook(app)
        }
    }
}

object Core {
    val dir =
        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/com.ks.apphook/cache/apphook/"
    const val ACTION = "com.ks.apphook.hookreceiver"

    private var isHook = false
    private var isCallHook = false

    /**
     * xposedhook
     */
    fun xposedHook(classLoader: ClassLoader) {
        if (isHook) return
        isHook = true
//        MethodHook
//            .Builder()
//            .setClass(Application::class.java)
//            .methodName("onCreate")
//            .afterHookedMethod {
//                if (isCallHook) return@afterHookedMethod
//                isCallHook = true
//                val app = it.thisObject as Application
//                val file = File("${dir}${app.packageName}")
//                Log.d(
//                    "wyz",
//                    "processName  " + app.packageName + " " + file.absolutePath + "  " + file.exists()
//                )
//                if (file.exists()) {
//                    log("hook${app.packageName}")
//                    appHook(app)
//                }
//            }
//            .build()
//            .execute()
        MethodHook
            .Builder()
            .setClass(Application::class.java)
            .methodName("attach")
            .parameterTypes(Context::class.java)
            .afterHookedMethod {
                log("执行hook1")
                val context = it.args[0] as Context
                val app = it.thisObject as Application
                log("hook${app.packageName}")
                Utils.init(app)
                log("执行hook2")
                SharedPreferencesUtils.init(context)
                log("执行hook3")
                CallActionManager.init()
                log("执行hook4")
                WebSocketManager.onOpen(context)
                log("执行hook5")
                LocationHookUtils.startHook(classLoader)
                log("执行hook6")
                DexHelper.loadDex(context)
                log("执行hook7")
                InitManager.init(app)
                log("执行hook8")
                HookManager.register()
                log("执行hook9")
                HookManager.hookAll(classLoader)
                log("执行hook10")
            }
            .build()
            .execute()
    }

    /**
     * 应用级hook
     */
    fun appHook(application: Application) {
//        hookInit(application)
        val context = application.applicationContext
        val app = application
        val classLoader = application.classLoader
        Utils.init(app)
        SharedPreferencesUtils.init(context)
        CallActionManager.init()
        WebSocketManager.onOpen(context)
        LocationHookUtils.startHook(classLoader)
//        DexHelper.loadDex(context)
        InitManager.init(app)
        HookManager.register()
        HookManager.hookAll(classLoader)
        log("hook完成!!")
    }

//    private fun hookInit(application: Application) {
//        try {
//            SandHookConfig.DEBUG = BuildConfig.DEBUG
//            SandHookConfig.delayHook = false
//            XposedCompat.cacheDir = File(application.cacheDir, "hook_cache")
//            XposedCompat.classLoader =
//                ProxyClassLoader(XposedBridge::class.java.classLoader, application.classLoader)
//            XposedCompat.context = application
//        } catch (t: Throwable) {
//            t.printStackTrace()
//        }
//    }

    fun isHook(context: Context): Boolean {
        val packageName = context.packageName
        val uri: Uri = Uri.parse("content://com.ks.apphook")
        val cursor = context.contentResolver.query(uri, null, "PKG=?", arrayOf(packageName), null)
        val count = cursor?.count ?: 0
        cursor?.close()
        log("hook开始判断${packageName}  $count")
        return count > 0
    }
}