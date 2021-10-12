package com.digexco.arch.services.logs


import com.digexco.arch.BuildConfig
import com.digexco.arch.services.logs.timber.CrashReportingTree
import com.digexco.arch.services.logs.timber.PrettyLoggingTree
import com.digexco.common.ILoggable
import timber.log.Timber

internal class TimberLogger : ILoggable {
    companion object {
        init {
            Timber.plant(
                if (BuildConfig.DEBUG)
                    PrettyLoggingTree()
                else
                    CrashReportingTree()
            )
        }
    }

    override fun d(tag: String, message: String) {
        Timber.tag(tag).d(message)
    }

    override fun i(tag: String, message: String) {
        Timber.tag(tag).i(message)
    }

    override fun w(tag: String, message: String) {
        Timber.tag(tag).w(message)
    }

    override fun e(tag: String, throwable: Throwable) {
        Timber.tag(tag).e(throwable)
    }

    override fun e(tag: String, throwable: Throwable, message: String) {
        Timber.tag(tag).e(throwable, message)
    }

    override fun e(tag: String, message: String) {
        Timber.tag(tag).e(message)
    }
}
