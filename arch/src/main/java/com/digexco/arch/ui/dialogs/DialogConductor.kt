package com.digexco.arch.ui.dialogs

import com.digexco.arch.DialogTypes
import com.digexco.arch.R
import com.digexco.arch.helpers.tryGetValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import me.aartikov.sesame.localizedstring.LocalizedString
import me.aartikov.sesame.property.PropertyHost
import me.aartikov.sesame.property.command
import java.util.*
import kotlin.coroutines.resume

@Suppress("unused")
class DialogConductor(override val propertyHostScope: CoroutineScope) : PropertyHost {
    val showAlert = command<Pair<DialogTypes, Map<String, Any>>>()

    private val callbacks = mutableMapOf<UUID, (DialogResult) -> Unit>()

    suspend fun alert(
        title: LocalizedString = LocalizedString.empty(),
        description: LocalizedString = LocalizedString.empty(),
        positiveAnswer: LocalizedString = LocalizedString.empty(),
        negativeAnswer: LocalizedString = LocalizedString.empty(),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        copyable : Boolean = false,
        cancelable : Boolean = true,
    ) = suspendCancellableCoroutine<DialogResult> { continuation->
        alert(title, description, positiveAnswer, negativeAnswer, neutralAnswer, copyable, cancelable) {
            if (!continuation.isCompleted)
                continuation.resume(it)
        }
    }

    suspend fun date(
        title: LocalizedString = LocalizedString.empty()
    ) = suspendCancellableCoroutine<DialogResult> { continuation->
        date(title) {
            if (!continuation.isCompleted)
                continuation.resume(it)
        }
    }

    suspend fun sheet(
        title: LocalizedString = LocalizedString.empty(),
        items: List<LocalizedString>,
        negativeAnswer: LocalizedString = LocalizedString.resource(R.string.cancel),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        cancelable : Boolean = true
    ) = suspendCancellableCoroutine<DialogResult> { continuation->
        sheet(title, items, negativeAnswer, neutralAnswer, cancelable) {
            if (!continuation.isCompleted)
                continuation.resume(it)
        }
    }

    suspend fun text(
        title: LocalizedString = LocalizedString.empty(),
        description: LocalizedString = LocalizedString.empty(),
        positiveAnswer: LocalizedString = LocalizedString.empty(),
        negativeAnswer: LocalizedString = LocalizedString.empty(),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        hint: LocalizedString = LocalizedString.empty(),
        cancelable : Boolean = true
    ) = suspendCancellableCoroutine<DialogResult> { continuation->
        text(title, description, positiveAnswer, negativeAnswer, neutralAnswer, hint, cancelable) {
            if (!continuation.isCompleted)
                continuation.resume(it)
        }
    }

    fun alert(
        title: LocalizedString = LocalizedString.empty(),
        description: LocalizedString = LocalizedString.empty(),
        positiveAnswer: LocalizedString = LocalizedString.empty(),
        negativeAnswer: LocalizedString = LocalizedString.empty(),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        copyable : Boolean = false,
        cancelable : Boolean = true,
        onResult: (DialogResult) -> Unit,
    ) {
        val map: Map<String, Any> = mapOf(
            DialogResolver.TITLE to title,
            DialogResolver.DESCRIPTION to description,
            DialogResolver.POSITIVE_ANSWER to positiveAnswer,
            DialogResolver.NEGATIVE_ANSWER to negativeAnswer,
            DialogResolver.NEUTRAL_ANSWER to neutralAnswer,
            DialogResolver.COPYABLE to copyable,
            DialogResolver.CANCELABLE to cancelable
        )

        showAlert(Pair(DialogTypes.alert, prepare(map, onResult)))
    }

    fun sheet(
        title: LocalizedString = LocalizedString.empty(),
        items: List<LocalizedString>,
        negativeAnswer: LocalizedString = LocalizedString.resource(R.string.cancel),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        cancelable : Boolean = true,
        onResult: (DialogResult) -> Unit
    ) {
        val map: Map<String, Any> =
            mapOf(
                DialogResolver.TITLE to title,
                DialogResolver.ARRAY to items,
                DialogResolver.NEGATIVE_ANSWER to negativeAnswer,
                DialogResolver.NEUTRAL_ANSWER to neutralAnswer,
                DialogResolver.CANCELABLE to cancelable
            )
        showAlert(Pair(DialogTypes.sheet, prepare(map, onResult)))
    }

    fun date(
        title: LocalizedString = LocalizedString.empty(),
        onResult: (DialogResult) -> Unit
    ) {
        val map: Map<String, Any> =
            mapOf(
                DialogResolver.TITLE to title
            )
        showAlert(Pair(DialogTypes.date, prepare(map, onResult)))
    }

    fun text(
        title: LocalizedString = LocalizedString.empty(),
        description: LocalizedString = LocalizedString.empty(),
        positiveAnswer: LocalizedString = LocalizedString.empty(),
        negativeAnswer: LocalizedString = LocalizedString.empty(),
        neutralAnswer: LocalizedString = LocalizedString.empty(),
        hint: LocalizedString = LocalizedString.empty(),
        cancelable : Boolean = true,
        onResult: (DialogResult) -> Unit
    ) {
        val map: Map<String, Any> = mapOf(
            DialogResolver.TITLE to title,
            DialogResolver.DESCRIPTION to description,
            DialogResolver.POSITIVE_ANSWER to positiveAnswer,
            DialogResolver.NEGATIVE_ANSWER to negativeAnswer,
            DialogResolver.NEUTRAL_ANSWER to neutralAnswer,
            DialogResolver.HINT to hint,
            DialogResolver.CANCELABLE to cancelable
        )
        showAlert(Pair(DialogTypes.text, prepare(map, onResult)))
    }

    fun toast(message: Any, lengthLong: Boolean = false) {
        val dict: Map<String, Any> = mapOf(
            DialogResolver.TITLE to message,
            DialogResolver.LENGTH to lengthLong
        )
        showAlert(Pair(DialogTypes.toast, dict))
    }

    fun showLoading(title: LocalizedString = LocalizedString.empty()) =
        showAlert(Pair(DialogTypes.loading, mapOf(DialogResolver.TITLE to title)))

    fun hideLoading() = showAlert(Pair(DialogTypes.loading, mapOf(DialogResolver.HIDE to true)))

    private fun prepare(
        map: Map<String, Any>,
        onResult: (DialogResult) -> Unit,
    ): Map<String, Any> {
        val id = UUID.randomUUID()
        val newMap = map.toMutableMap()
        newMap[DialogResolver.ID] = id.toString()
        callbacks[id] = onResult
        return newMap
    }

    fun onDialogAction(id : UUID, result : DialogResult) {
        val callback: ((DialogResult) -> Unit)? = callbacks.tryGetValue(id)
        callback?.invoke(result)
    }
}
