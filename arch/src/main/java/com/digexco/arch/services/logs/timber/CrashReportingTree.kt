package com.digexco.arch.services.logs.timber

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

internal class CrashReportingTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        when (priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN -> return
        }

        val t = throwable ?: Exception(message)
        val crashlytics = FirebaseCrashlytics.getInstance()

        crashlytics.setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
        tag?.let { crashlytics.setCustomKey(CRASHLYTICS_KEY_TAG, it) }
        crashlytics.setCustomKey(CRASHLYTICS_KEY_MESSAGE, message)
        crashlytics.recordException(t)
    }

    companion object {
        private val CRASHLYTICS_KEY_PRIORITY = "priority"
        private val CRASHLYTICS_KEY_TAG = "tag"
        private val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
