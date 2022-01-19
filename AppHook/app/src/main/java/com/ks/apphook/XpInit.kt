package com.ks.apphook

import android.os.Environment
import android.util.Log
import com.ks.core.Core
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

//class MyContextScope(context: CoroutineContext) : CoroutineScope {
//    override val coroutineContext: CoroutineContext = context
//}
//
//val mainScope by lazy {
//    val dispatcherFactoryClzz = Class.forName("kotlinx.coroutines.android.AndroidDispatcherFactory")
//    val factoryObj = dispatcherFactoryClzz.newInstance()
//    val method = dispatcherFactoryClzz.getMethod("createDispatcher", List::class.java)
//    val invoke = method.invoke(factoryObj, mutableListOf<Any>()) as CoroutineContext
//    val myContextScope = MyContextScope(SupervisorJob() + invoke)
//    myContextScope
//}

class XpInit : IXposedHookLoadPackage {
//    val dir =
//        Environment.getExternalStorageDirectory().absolutePath + "/Android/data/com.ks.apphook/cache/apphook/"
//    val hide = arrayListOf<String>("com.ks.apphook")
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        Log.d("wyz", "开始Hook应用程序${lpparam.processName}  ${lpparam.packageName}")
        if (lpparam.processName == lpparam.packageName) {
            Log.d("wyz", "确认开始hook${lpparam.processName}")
            Core.xposedHook(lpparam.classLoader)
        }
    }
}