package com.digexco.arch.ui.helpers

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.digexco.arch.helpers.CoroutineCommand
import com.digexco.arch.helpers.CoroutineCommandValue
import com.google.android.material.textfield.TextInputLayout


const val TAB = "\t"

fun hideTemporaryView(view: View, animationDuration: Long = 300L) =
    showTemporaryView(view, animationDuration, 0)

fun showTemporaryView(view: View, animationDuration: Long = 300L, visibleDuration: Long = 3000L) {
    view.apply {
        if (visibility == View.GONE && visibleDuration == 0L) return@apply
        if (visibility != View.VISIBLE) {
            alpha = 0f
            visibility = View.VISIBLE

            animate().apply {
                alpha(1f)
                duration = animationDuration
            }
        }

        val savedAnimator = tag as ValueAnimator?
        savedAnimator?.cancel()

        val animator = ValueAnimator.ofFloat(1f, 0f).apply {
            duration = animationDuration
            addUpdateListener {
                val value = (it.animatedValue as Float)
                alpha = value
                if (value <= 0f) visibility = View.GONE
            }
            startDelay = visibleDuration
            start()
        }
        tag = animator
    }
}


fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) view = View(this)
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.showKeyboard(view: View) {
    view.requestFocus()
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun TextView.setOnEditorAction(editorActionId: Int, listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        return@setOnEditorActionListener if (actionId == editorActionId) {
            listener()
            true
        } else
            false
    }
}

fun Activity.controlWindowInsets(hide: Boolean) {
    // WindowInsetsController can hide or show specified system bars.
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    val behavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
    val type = WindowInsetsCompat.Type.systemBars()
    insetsController.systemBarsBehavior = behavior
    if (hide) {
        insetsController.hide(type)
    } else {
        insetsController.show(type)
    }
}

fun View.bindClick(command: CoroutineCommand) {
    setOnClickListener { command() }
}

fun <T> View.bindClickValue(command: CoroutineCommandValue<T>, getValue: () -> T) {
    setOnClickListener { command(getValue()) }
}

fun View.bindLongClick(command: CoroutineCommand) {
    setOnLongClickListener {
        command()
        true
    }
}

fun TextView.bindEditorAction(editorActionId: Int, command: CoroutineCommand) {
    setOnEditorAction(editorActionId) { command() }
}

fun Fragment.getColor(@ColorRes color: Int): Int {
    return activity?.let { resources.getColor(color, it.theme) } ?: Color.RED
}

private fun isPrintable(c: String, paint: Paint) = paint.hasGlyph(c)
fun replaceUnprintableChars(s: String, paint: Paint) =
    s.map {
        val c = it.toString()
        if (isPrintable(c, paint)) c else "\t"
    }.joinToString("").trim()

fun replaceUnprintableBarcodeChars(s: String?) =
    s?.replace(Char(0x1d).toString(), TAB)
        ?.replace(Char(0xe8).toString(), TAB)?.trim()


var TextInputLayout.text: String
    get() {
        return editText?.text?.toString().orEmpty()
    }
    set(value) {
        editText?.apply {
            setText(value)
            setSelection(text.length)
        }
    }



fun TextView.setTextFormatted(@StringRes id: Int, vararg formatArgs: Any) {
    text = resources.getString(id, *formatArgs)
}
