package com.digexco.arch.helpers

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleCoroutineScope
import com.digexco.common.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import me.aartikov.sesame.localizedstring.LocalizedString
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.Charset


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


fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

val Int.localized : LocalizedString
    get() = LocalizedString.resource(this)

fun Int.localized(vararg args : Any) : LocalizedString
        = LocalizedString.resource(this, *args)

val CharSequence.localized : LocalizedString
    get() = LocalizedString.raw(this)

val LocalDateTime.Companion.current: LocalDateTime
    get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

val Uri.extension: String
    get() = path!!.substringAfterLast('.', "")

val Uri.fileName: String
    get() = path!!.substringAfterLast('/', "")

val File.fileName: String
    get() = absolutePath.substringAfterLast('/', "")

fun Uri.getOriginalFileName(context: Context): String? {
    return try {
        context.contentResolver.query(this, null, null, null, null)?.use {
            val nameColumnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameColumnIndex)
        }
    } catch (t: Throwable) {
        path!!.substringAfterLast('/', "")
    }
}


fun File.writeInputString(ins: InputStream) {
    try {
        FileOutputStream(this).use { os ->
            val buffer = ByteArray(4096)
            var length: Int
            while (ins.read(buffer).also { length = it } > 0) {
                os.write(buffer, 0, length)
            }
            os.flush()
        }
    } catch (ex: java.lang.Exception) {
        Logger.e("Save File", ex)
        ex.printStackTrace()
    } finally {
        ins.close()
    }
}

fun <E> Iterable<E>.replace(old: E, new: E) = map { if (it == old) new else it }
