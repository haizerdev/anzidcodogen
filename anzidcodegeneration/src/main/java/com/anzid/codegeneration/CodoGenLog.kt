package com.anzid.codegeneration

import java.io.File
import java.io.FileWriter
import java.io.IOException

object CodoGenLog {
    private const val LOG_FILE = "C:/anzid/codogen/logs.txt"
    private const val SEPARATION_LOG = "==================================================================="

    fun log(message: Any? = null, isSeparationLog: Boolean = false) {
        when {
            isSeparationLog and (message == null) -> writeToFile(SEPARATION_LOG)
            isSeparationLog.not() and (message != null) -> writeToFile(message.toString())
            else -> return
        }
    }

    private fun writeToFile(message: String) {
        File(LOG_FILE).parentFile.mkdirs()
        FileWriter(LOG_FILE, true).use {
            it.append(message)
            it.append("\n")
        }
    }
}