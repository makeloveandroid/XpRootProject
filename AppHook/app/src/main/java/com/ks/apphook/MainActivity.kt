package com.ks.apphook

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.AppUtils
import com.ks.apphook.dao.DaoManager
import com.ks.core.log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    companion object{
        var isShow = true
    }
    val DIR by lazy {
        externalCacheDir!!.absolutePath + "/apphook/" ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DaoManager.init(applicationContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1001)
            }
        }
        launch {
            val dialog = ProgressDialog(this@MainActivity)
            dialog.setTitle("正在初始化....")
            dialog.setCancelable(false)
            dialog.show()
            val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
            recyclerView.layoutManager = LinearLayoutManager(applicationContext, VERTICAL, false).apply {
                isItemPrefetchEnabled = false
                recycleChildrenOnDetach = true
            }
            recyclerView.adapter = AppAdapter(getInstallApps()) { appInfo ->
                if (appInfo.isHook) {
                    showHintDialog("取消拦截 [${appInfo.name}]?",
                        DialogInterface.OnClickListener { dialog, which ->
//                            DaoManager.deleteHookApp(appInfo.pkg)
                            File(DIR, appInfo.pkg).delete()
                            appInfo.isHook = false
                            dialog.dismiss()
                            recyclerView.adapter!!.notifyDataSetChanged()
                        })
                } else {
                    showHintDialog("确认拦截 [${appInfo.name}]?", DialogInterface.OnClickListener { dialog, which ->
//                        DaoManager.saveHookApp(appInfo)
                        val mk = File(DIR + "/${appInfo.pkg}/").mkdirs()
                        log("创建:$mk ${File(DIR, appInfo.pkg).absolutePath}")
                        appInfo.isHook = true
                        dialog.dismiss()
                        recyclerView.adapter!!.notifyDataSetChanged()
                    })
                }
            }
            dialog.dismiss()
        }

//        recyclerview.addOnScrollListener(object :RecyclerView.OnScrollListener(){
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                Log.d("wyz","top:${dy}")
//                if (dy == 0) {
//                    Log.d("wyz","top!")
//                }2
//
//            }
//        })

        test.setOnClickListener {
            launch {
                val adapter = recyclerview.adapter as AppAdapter
                val linearLayoutManager = recyclerview.layoutManager as LinearLayoutManager
                linearLayoutManager.scrollToPositionWithOffset(10, 200)

//                val installApps = getInstallApps()
//                adapter.items.add(0, installApps.get(installApps.size - 1))
//                adapter.notifyItemRemoved(0)
//                adapter.notifyItemInserted(1)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1001) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private suspend fun getInstallApps(): MutableList<AppInfo> {
        return withContext(Dispatchers.Default) {
            val appsInfo = AppUtils.getAppsInfo()
            log("当前安装app:${appsInfo.size}")

            val datas = mutableListOf<AppInfo>()
            appsInfo.forEach {
//                DaoManager.haveHookApp(it.packageName)

                if (File(DIR, it.packageName).exists()) {
                    datas.add(
                        0, AppInfo(
                            it.name,
                            it.icon,
                            it.packageName,
                            it.packagePath,
                            true
                        )
                    )
                } else {
                    datas.add(
                        AppInfo(
                            it.name,
                            it.icon,
                            it.packageName,
                            it.packagePath,
                            false
                        )
                    )
                }

            }
            datas
        }
    }

    fun showHintDialog(msg: String, click: DialogInterface.OnClickListener): Dialog {
        return AlertDialog
            .Builder(this)
            .setMessage(msg)
            .setNegativeButton("确定", click)
            .setNeutralButton("老子点错啦") { dialog, which ->
                dialog.dismiss()
            }.show()
    }

    fun click(view: View) {
        isShow = false
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val transition: TransitionSet = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(Fade())
        TransitionManager.beginDelayedTransition(recyclerView.parent as ViewGroup, transition)
        view.visibility = if (view.visibility == View.INVISIBLE) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

    }
}


class AppAdapter(val items: MutableList<AppInfo>, val clickItem: (AppInfo) -> Unit) :
    RecyclerView.Adapter<AppItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppItemViewHolder {
        Log.d("wyz", "onCreateViewHolder")
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_app_manage, parent, false)
        return AppItemViewHolder(itemView, clickItem)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        Log.d("wyz", "onBindViewHolder")
        val appInfo = items[position]
        holder.bind(appInfo)
    }

}

class AppItemViewHolder(itemView: View, val clickItem: (AppInfo) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    var icon: ImageView = itemView.findViewById(R.id.item_app_icon)
    var itemAppName: TextView = itemView.findViewById(R.id.item_app_name)

    fun bind(appInfo: AppInfo) {
        if (!MainActivity.isShow) {
            icon.visibility = View.GONE
        }
        icon.setImageDrawable(appInfo.icon)
        itemAppName.text = appInfo.name
        if (appInfo.isHook) {
            itemView.setBackgroundColor(0xFF03DAC5.toInt())
            itemView.setOnClickListener {
                clickItem(appInfo)
            }
        } else {
            itemView.setBackgroundColor(Color.WHITE)
            itemView.setOnClickListener {
                clickItem(appInfo)
            }
        }
    }
}


