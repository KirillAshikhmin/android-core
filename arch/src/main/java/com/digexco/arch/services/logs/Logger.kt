package com.digexco.arch.services.logs

import com.digexco.common.ILoggable

@Suppress("SpreadOperator", "unused", "MemberVisibilityCanBePrivate")
object LoggerImpl : ILoggable {

    val flowLogger = FlowLogger()

    var isFullLogEnabled = true

    private var logMerger = LogMerger

    init {
        LogMerger.loggers.add(flowLogger)
        LogMerger.loggers.add(TimberLogger())
    }

    override fun d(tag: String, message: String) {
        if (isFullLogEnabled)
            LogMerger.d(tag, message = message)
    }

    fun d(tag: String, lazyMessage: () -> String) {
        if (isFullLogEnabled)
            LogMerger.d(tag, message = lazyMessage.invoke())
    }

    override fun i(tag: String, message: String) {
        if (isFullLogEnabled)
            LogMerger.i(tag, message = message)
    }

    override fun w(tag: String, message: String) {
        if (isFullLogEnabled)
            LogMerger.w(tag, message = message)
    }

    override fun e(tag: String, throwable: Throwable, message: String) {
        LogMerger.e(tag, throwable, message = message)
    }

    override fun e(tag: String, throwable: Throwable) {
        LogMerger.e(tag, throwable)
    }

    override fun e(tag: String, message: String) {
        LogMerger.e(tag, message)
    }

    fun printStackTrace(tag: String) {
        if (isFullLogEnabled) {
            Thread.currentThread().stackTrace.forEach { d(tag, it.toString()) }
        }
    }

    object LogMerger : ILoggable {

        val loggers = mutableListOf<ILoggable>()

        override fun d(tag: String, message: String) {
            loggers.forEach { it.d(tag, message) }
        }

        override fun i(tag: String, message: String) {
            loggers.forEach { it.i(tag, message) }
        }

        override fun w(tag: String, message: String) {
            loggers.forEach { it.w(tag, message) }
        }

        override fun e(tag: String, throwable: Throwable) {
            loggers.forEach { it.e(tag, throwable) }
        }

        override fun e(tag: String, throwable: Throwable, message: String) {
            loggers.forEach { it.e(tag, throwable, message) }
        }

        override fun e(tag: String, message: String) {
            loggers.forEach { it.e(tag, message) }
        }

    }
}

