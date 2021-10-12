package com.digexco.arch.services.logs.timber

import android.util.Log
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import timber.log.Timber


internal class PrettyLoggingTree : Timber.Tree() {
    // Workaround to render logs nicely (https://github.com/orhanobut/logger/issues/202)
    private val logStrategy =
        LogStrategy { priority, tag, message -> Log.println(priority, "\uD835\uDECC" + tag, message) }

    private val formatStrategy by lazy {
        PrettyFormatStrategy.newBuilder()
            .logStrategy(logStrategy)
            .methodCount(1)
            .showThreadInfo(false)
            .methodOffset(7)
            .tag("")
            .build()
    }

    init {
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        Logger.log(priority, tag, message, t)
    }
}
