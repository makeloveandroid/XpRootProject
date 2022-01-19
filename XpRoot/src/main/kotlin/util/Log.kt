package util

const val IS_DEBUG = true

object Log {
    private var block: ((String, String) -> Unit)? = null

    @JvmStatic
    @Synchronized
    fun d(tag: String, msg: String) {
        println("$tag : $msg")
        block?.invoke(tag, msg)
    }

    @JvmStatic
    fun e(tag: String, msg: String): Unit {
        println("$tag : $msg")
    }

    @JvmStatic
    @Synchronized
    fun setTextBack(block: (String, String) -> Unit) {
        this.block = block
    }
}