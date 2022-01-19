package gui

import com.intellij.util.ReflectionUtil
import java.io.InputStream

object FileUtil {
    fun getResourceAsStream(s: String): InputStream {
        val callerClass = ReflectionUtil.getGrandCallerClass()!!
        return callerClass.classLoader.getResourceAsStream(s)
    }
}