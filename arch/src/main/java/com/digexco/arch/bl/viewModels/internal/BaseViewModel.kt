package com.digexco.arch.bl.viewModels.internal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digexco.arch.ui.dialogs.DialogConductor
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.aartikov.sesame.property.PropertyHost

abstract class BaseViewModel : ViewModel(), PropertyHost {

    override val propertyHostScope: CoroutineScope get() = viewModelScope
    private var isInitialized: Boolean = false

    val dialog = DialogConductor(viewModelScope)

    abstract val router: Router

    var navigationParams: Map<String, Any> = mapOf()
    var resultKey: String? = null

    val liveDataProperties: MutableMap<String, MutableLiveData<*>> = mutableMapOf()

    fun setResult(data: Any) {
        if (resultKey.isNullOrEmpty()) throw NullPointerException("You're should pass result key")
        router.sendResult(resultKey!!, data)
    }

    fun runInScope(run: suspend () -> Unit) {
        viewModelScope.launch {
            run.invoke()
        }
    }


    fun navigateBack() {
        router.exit()
    }


    fun navigateTo(screen: Screen) {
        router.navigateTo(screen)
    }

    fun onViewCreated() {
        viewCreated()
        if (!isInitialized) {
            isInitialized = true
            onInitialized()
            viewModelScope.launch { loadData() }
        }
    }

    fun reloadData() {
        viewModelScope.launch { loadData() }
    }

    open fun onInitialized() {}
    open suspend fun loadData() {}
    open fun viewCreated() {}
    open fun viewClosed() {}

    fun resultReceived(result: Map<String, Any>) {
        onResultReceived(result)
    }

    open fun onResultReceived(result: Map<String, Any>) {

    }

    open fun close(back: Boolean = false): Boolean {
        router.exit()
        return true
    }
}
