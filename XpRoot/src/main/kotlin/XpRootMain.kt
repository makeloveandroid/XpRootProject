import kotlinx.coroutines.runBlocking
import org.apache.commons.cli.*
import task.TaskManager
import java.io.File
import java.lang.RuntimeException


/**
 * MLGB 磨磨唧唧写不完
 */
fun main(args: Array<String>) = runBlocking<Unit> {
    // headless
    System.setProperty("java.awt.headless", "true")
    val initOptions = initOptions()
    val parser: CommandLineParser = DefaultParser()

    try {
        var commandLine = parser.parse(initOptions, args, false)
        checkCommand(commandLine)
        TaskManager.call(commandLine)
    } catch (ex: ParseException) {
        System.err.println(ex.message)
        System.exit(1)
    }

}


fun checkCommand(commandLine: CommandLine) {
    if (!commandLine.hasOption("host")) {
        throw RuntimeException("没有注入APP,你注入个毛啊....")
    }

    if (!commandLine.hasOption("virus")) {
        throw RuntimeException("没有Xposed模块,你注入个毛啊....")
    }
    val host = commandLine.getOptionValue("host")
    if (host==null || !File(host).exists()) {
        throw RuntimeException("没有注入APP,你注入个毛啊....")
    }

    val virus = commandLine.getOptionValue("virus")
    if (virus == null || !File(virus).exists()) {
        throw RuntimeException("没有Xposed模块,你注入个毛啊....")
    }

}

fun initOptions(): Options {
    val options = Options()

    // create options
    val hostOption = Option.builder("host")
        .longOpt("host")
        .hasArg(true)
        .desc("注入的宿主路径")
        .build()

    val virusOption = Option.builder("virus")
        .longOpt("virus")
        .hasArg(true)
        .desc("注入的Xp模块")
        .build()

    val debugOption = Option.builder("debug")
        .longOpt("debug")
        .hasArg(true)
        .desc("是否启用debug模式")
        .build()

    val dexOption = Option.builder("dex")
            .longOpt("dex")
            .hasArg(true)
            .desc("是否注入宿主的Application代码块")
            .build()

    val install = Option.builder("install")
            .longOpt("install")
            .hasArg(true)
            .desc("是否安装签名后的Apk")
            .build()

    val adb = Option.builder("adbPath")
        .longOpt("adbPath")
        .hasArg(true)
        .desc("adb的路径")
        .build()


    options.addOption(dexOption)
    options.addOption(hostOption)
    options.addOption(debugOption)
    options.addOption(virusOption)
    options.addOption(install)
    options.addOption(adb)
    return options
}
