package com.digexco.arch.helpers

import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import me.aartikov.sesame.localizedstring.LocalizedString
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyHost
import me.aartikov.sesame.property.PropertyObserver
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty0


fun PropertyObserver.bindEditText(property: KMutableProperty0<String>, editText: EditText?) {
    val et = editText ?: return
    property bind {
        val current = et.text.toString()
        if (current != it) {
            et.setText("")
            et.append(it)
        }
    }
    et.addTextChangedListener {
        property.set(it.toString())
    }
}

fun PropertyObserver.bindText(property: KProperty0<String>, textView: TextView) {
    property bind {
        textView.text = it
    }
}

fun PropertyObserver.bindLocalizedText(property: KProperty0<LocalizedString>, textView: TextView) {
    property bind {
        textView.localizedText = it
    }
}

fun PropertyObserver.bindViewVisible(
    property: KProperty0<Boolean>,
    view: View,
    useInvisible: Boolean = false,
    animated: Boolean = false
) {
    property bind {
        view.setViewVisible(it, useInvisible, animated)
    }
}

fun PropertyHost.reverseCommand(action: suspend () -> Unit) =
    CoroutineCommand(action, this.propertyHostScope)
