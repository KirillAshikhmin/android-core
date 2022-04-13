package com.digexco.arch.helpers

import android.view.View
import android.widget.Button
import android.widget.CheckBox
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

fun PropertyObserver.bindCheckBox(property: KMutableProperty0<Boolean>, checkBox: CheckBox) {
    property bind {
        val current = checkBox.isChecked
        if (current != it) {
            checkBox.isChecked = it
        }
    }
    checkBox.setOnCheckedChangeListener { _, isChecked ->
        property.set(isChecked)
    }
}

fun PropertyObserver.bindCommand(
    commandProperty: KProperty0<CoroutineCommand>,
    button: Button,
    bindState: Boolean = false,
    checkInvoke: () -> Boolean = { true }
) {
    val command = commandProperty.get()
    button.setOnClickListener { if (checkInvoke())command() }

    if (bindState) {
        command::isCanExecute bind {
            button.isEnabled = it
        }
    }
}

fun PropertyObserver.bindCommandNonDisabled(
    commandProperty: KProperty0<CoroutineCommand>,
    button: Button,
    bindState: Boolean = false,
    whenCheckFailed: () -> Unit = { },
    checkInvoke: () -> Boolean = { true }
) {
    val command = commandProperty.get()
    button.setOnClickListener { if (checkInvoke()) command() else whenCheckFailed() }

    if (bindState) {
        command::isCanExecute bind {
            button.isActivated = it
        }
    }
}
fun PropertyObserver.bindCommandValueNonDisabled(
    commandProperty: KProperty0<CoroutineCommandValue<String>>,
    button: Button,
    getValue: () -> String,
    bindState: Boolean = false,
    whenCheckFailed: () -> Unit = { },
    checkInvoke: () -> Boolean = { true }
) {
    val command = commandProperty.get()
    button.setOnClickListener { if (checkInvoke()) command(getValue()) else whenCheckFailed() }

    if (bindState) {
        command::isCanExecute bind {
            button.isActivated = it
        }
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

fun PropertyHost.reverseCommand(isEnabled: Boolean = true, action: suspend () -> Unit) =
    CoroutineCommand(action, this.propertyHostScope, isEnabled)

fun <T> PropertyHost.reverseCommandValue(action: suspend (T) -> Unit) =
    CoroutineCommandValue(action, this.propertyHostScope)
