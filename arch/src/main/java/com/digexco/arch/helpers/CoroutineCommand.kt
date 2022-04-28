package com.digexco.arch.helpers

import androidx.annotation.MainThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.aartikov.sesame.property.PropertyHost
import me.aartikov.sesame.property.computed
import me.aartikov.sesame.property.state

class CoroutineCommand(
    private val action: suspend () -> Unit,
    override val propertyHostScope: CoroutineScope,
    isEnabled: Boolean = true
) :
    PropertyHost {

    private var isNotInvoking by state(true)

    var isEnabled by state(isEnabled)

    val isCanExecute by computed(
        ::isEnabled,
        ::isNotInvoking
    ) { isEnabled, isInvoking -> isEnabled && isInvoking }


    @MainThread
    private fun execute() {
        if (!isCanExecute) return

        propertyHostScope.launch {
            executeSuspend()
        }
    }

    @MainThread
    private suspend fun executeSuspend() {
        if (!isCanExecute) return

        isNotInvoking = false
        action()
        isNotInvoking = true
    }

    @MainThread
    operator fun invoke() {
        execute()
    }
}

class CoroutineCommandValue<T>(
    private val action: suspend (T) -> Unit,
    override val propertyHostScope: CoroutineScope,
    isEnabled: Boolean = true
) :
    PropertyHost {


    private var isNotInvoking by state(true)

    var isEnabled by state(isEnabled)

    val isCanExecute by computed(
        ::isEnabled,
        ::isNotInvoking
    ) { isEnabled, isInvoking -> isEnabled && isInvoking }


    @MainThread
    private fun execute(value: T) {
        if (!isCanExecute) return
        propertyHostScope.launch {
            executeSuspend(value)
        }
    }

    @MainThread
    private suspend fun executeSuspend(value: T) {
        if (!isCanExecute) return

        isNotInvoking = false
        action(value)
        isNotInvoking = true
    }

    @MainThread
    operator fun invoke(value: T) {
        execute(value)
    }
}
