package com.digexco.arch.services.logs

import android.util.Log
import androidx.annotation.NonNull
import com.digexco.common.ILoggable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.util.*

class FlowLogger : ILoggable {
    private val mutableFlow: MutableSharedFlow<String> =
        MutableSharedFlow(50, 10, BufferOverflow.DROP_OLDEST)
    val flow: SharedFlow<String> = mutableFlow.asSharedFlow()

    fun log(priority: Int, tag: String?, @NonNull message: String, t: Throwable?) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss a", Locale.US)
        val currentDate = sdf.format(Date())
        val level = when (priority) {
            Log.VERBOSE -> Log.VERBOSE.toString()
            Log.INFO -> Log.INFO.toString().toString()
            Log.DEBUG -> Log.DEBUG.toString()
            Log.WARN -> Log.WARN.toString()
            Log.ERROR -> Log.ERROR.toString()
            else -> "Priority: $priority"
        }
        val sb = StringBuilder(currentDate).append(" ").append(level).append(":\n")
        tag?.let {
            sb.append(it).append("\n")
        }
        sb.append(message)
        t?.let {
            sb.append("\n").append(t.toString()).append("\n").append(t.stackTraceToString())
        }
        mutableFlow.tryEmit(sb.toString())
    }

    override fun d(tag: String, message: String) {
        log(Log.DEBUG, tag, message, null)
    }

    override fun i(tag: String, message: String) {
        log(Log.INFO, tag, message, null)
    }

    override fun w(tag: String, message: String) {
        log(Log.WARN, tag, message, null)
    }

    override fun e(tag: String, throwable: Throwable) {
        log(Log.ERROR, tag, "", throwable)
    }

    override fun e(tag: String, throwable: Throwable, message: String) {
        log(Log.ERROR, tag, message, throwable)
    }

    override fun e(tag: String, message: String) {
        log(Log.ERROR, tag, message, null)
    }
}
