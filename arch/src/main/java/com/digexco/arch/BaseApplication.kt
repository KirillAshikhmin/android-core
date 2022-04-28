package com.digexco.arch

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import com.digexco.arch.helpers.ArchFragmentFactory
import com.digexco.arch.services.logs.LoggerImpl
import com.digexco.common.Logger

abstract class BaseApplication : Application(), Application.ActivityLifecycleCallbacks {

    companion object {
        private const val TAG = "Application"
    }

    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate() {
        super.onCreate()
        Logger.setImplementation(LoggerImpl)
        val mainPackage = packageName.substring(0, packageName.lastIndexOf("."))
        fragmentFactory = ArchFragmentFactory(classLoader, mainPackage)
        registerActivityLifecycleCallbacks(this)
    }


    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        if (activity is FragmentActivity)
            activity.supportFragmentManager.fragmentFactory = fragmentFactory
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(activity: Activity) {
    }
}
