package gui

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import main

object XpRootMainCore {
    fun call(args: Array<String>) = runBlocking {
        main(args)
    }
}