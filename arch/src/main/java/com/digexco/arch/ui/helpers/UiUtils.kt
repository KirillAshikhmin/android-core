package com.digexco.arch.ui.helpers

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.digexco.arch.helpers.CoroutineCommand


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
fun View.bindLongClick(command: CoroutineCommand) {
    setOnLongClickListener {
        command()
        true
    }
}
fun TextView.bindEditorAction(editorActionId: Int, command: CoroutineCommand) {
    setOnEditorAction(editorActionId) { command() }
}
