package com.digexco.common

object Logger : ILoggable {

    private var logger: ILoggable? = null

    fun setImplementation(logger: ILoggable) {
        Logger.logger = logger
    }

    override fun d(tag: String, message: String) {
        logger?.d(tag, message = message)
    }

    fun d(tag: String, lazyMessage: () -> String) {
        logger?.d(tag, message = lazyMessage.invoke())
    }

    override fun i(tag: String, message: String) {
        logger?.i(tag, message = message)
    }

    override fun w(tag: String, message: String) {
        logger?.w(tag, message = message)
    }

    override fun e(tag: String, throwable: Throwable, message: String) {
        logger?.e(tag, throwable, message = message)
    }

    override fun e(tag: String, throwable: Throwable) {
        logger?.e(tag, throwable)
    }
    override fun e(tag: String,  message: String) {
        logger?.e(tag, message)
    }
}
