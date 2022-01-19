# 关于项目介绍
整体来说代码量还是很大的！！

## app_hook_macos
Flutter应用端程序（接收器）

## AppHook

```
AppHook
└───App（Hook模块，兼容Xposed模块）
└───dexinjetcore（XpRoot.start()方法，负责加载Hook模块&Hook插件）
└───core（Hook核心逻辑，所有的Hook方法都在此）
└───xposedcompat（注入的应用Hook框架）
```

## KwaiApkRootPlugin

AndroidStudio 插件。

## XpRoot
Java感染程序，负责APK反编译&注入&修改&回编译&安装等。

### 如果要感染其他应用
执行 gui.GuiMain.main() 方法。

需要传递一个参数 `Hook模块路径` 上面的 `AppHook app`  打包出来的 APK.  

> 当然也可以感染其他Xposed模块。

### 命令感染
XpRootMain.main() 方法。  

- 基本命令
```
java -jar ./ApkRoot.jar -host ./宿主.apk -virus ./xposed模块.apk
```
- debug 命令
修改宿主 Apk 是否变为 debug 模式
```
java -jar ./ApkRoot.jar -host ./宿主.apk -virus ./xposed模块.apk -debug 1
```
- dex 命令
通过直接修改宿主Application注入入口 (可能存在65535问题)
```
java -jar ./ApkRoot.jar -host ./宿主.apk -virus ./xposed模块.apk -dex 1
```

### 关于签名
1. 如果要使用自己的签名文件，修改 `XpRoot/src/main/resources/keystore` 文件。
2. 修改签名代码 `SignApkTask.kt`
