package com.digexco.arch.helpers

import android.animation.Animator
import android.app.Activity
import android.graphics.*
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


fun Activity.setButtonClickListener(@IdRes buttonId: Int, listener: (View?) -> Unit) {
    findViewById<View>(buttonId).setOnClickListener { v -> listener(v) }
}
fun Activity.setButtonClickListener(@IdRes buttonId: Int, listener: View.OnClickListener) {
    findViewById<View>(buttonId).setOnClickListener(listener)
}

fun View.setButtonClickListener(@IdRes buttonId: Int, listener: (View?) -> Unit) {
    findViewById<View>(buttonId).setOnClickListener { v -> listener(v) }
}

fun Activity.setViewVisible(@IdRes viewId: Int, visible: Boolean) {
    findViewById<View>(viewId).visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setViewVisible(
    @IdRes viewId: Int,
    visible: Boolean,
    useInvisible: Boolean = false,
    animated: Boolean = false
) {
    findViewById<View>(viewId).setViewVisible(visible, useInvisible, animated)
}

fun View.setViewVisible(
    visible: Boolean,
    useInvisible: Boolean = false,
    animated: Boolean = false
) {
    val targetVisibility =
        if (visible) View.VISIBLE else (if (useInvisible) View.INVISIBLE else View.GONE)

    if (visibility == targetVisibility) return
    if (!animated) {
        visibility =
            if (visible) View.VISIBLE else (if (useInvisible) View.INVISIBLE else View.GONE)
    } else {
        val endAlpha = if (visible) 1f else 0f
        val startAlpha = if (visible) 0f else 1f
        alpha = startAlpha
        visibility = View.VISIBLE
        animate().alpha(endAlpha).setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                visibility =
                    if (visible) View.VISIBLE else (if (useInvisible) View.INVISIBLE else View.GONE)
            }
        })
    }
}

fun <T> Flow<T>.onEachInScope(scope: LifecycleCoroutineScope, action: suspend (T) -> Unit) {
    scope.launchWhenStarted {
        this@onEachInScope.onEach(action).launchIn(scope)
    }
}
