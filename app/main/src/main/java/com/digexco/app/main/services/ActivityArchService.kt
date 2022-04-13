package com.digexco.app.main.services

import android.app.Activity
import java.lang.ref.WeakReference

open class ActivityArchService {
    private var _activityChangeListener: ((Activity?) -> Unit)? = null
    private var _activityList : MutableList<WeakReference<Activity>> = mutableListOf()
    val activity : Activity
        get() = _activityList.lastOrNull()?.get() ?: throw NullPointerException("You should call setActivity")

    fun setActivityChangeListener(listener: (Activity?) -> Unit) {
        _activityChangeListener = listener
    }

    fun removeActivityChangeListener() {
        _activityChangeListener = null
    }

    fun setActivity (activity: Activity){
        _activityList.add(WeakReference(activity))
        _activityChangeListener?.invoke(_activityList.lastOrNull()?.get())
    }

    fun destroyActivity (activity: Activity){
        val found = _activityList.firstOrNull { r->r.get() == activity }
        found?.let {
            it.clear()
            _activityList.remove(it)
            _activityChangeListener?.invoke(_activityList.lastOrNull()?.get())
        }
    }
}
