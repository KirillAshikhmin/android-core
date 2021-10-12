package com.digexco.arch.helpers

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class CoroutineCommand(private val action: suspend () -> Unit, val scope: CoroutineScope) {

    private var canExecute = true

    val isCanExecute: Boolean
        get() = canExecute

    private fun execute() {
        if (!canExecute) return
        scope.launch {
            executeSuspend()
        }
    }

    private suspend fun executeSuspend() {
        if (!canExecute) return
        canExecute = false
        action()
        canExecute = true
    }

    operator fun invoke() {
        execute()
    }
}
