package com.klaviyo.utils

import android.content.Context
import java.util.logging.Level
import java.util.logging.Logger as Log

/**
 * Provides logging functionality with automatic tagging based on stack trace
 */
object Logger {

    private lateinit var packageName: String

    /**
     * Configures the [Logger] using the given [context]
     */
    fun configure(context: Context) {
        this.packageName = context.packageName
    }

    private fun getRelevantStack(): StackTraceElement? {
        return Thread.currentThread().stackTrace.firstOrNull {
            it.className.startsWith(this.packageName)
        }
    }

    /**
     * Logs a [message] with the given [level] to a [Logger] tagged with the calling class name
     */
    fun log(level: Level = Level.INFO, message: String) {
        val stack = getRelevantStack()
        val tag = stack?.let {
            val trimmedName = it.className.replace("${this.packageName}.", "")
            if (it.methodName == "invoke") "$trimmedName:${it.lineNumber}" else trimmedName
        } ?: this.packageName
        Log.getLogger(tag).log(level, message)
    }

}