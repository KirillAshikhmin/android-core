package com.digexco.arch.bl.viewModels.internal

import androidx.annotation.MainThread
import com.digexco.arch.bl.viewModels.internal.states.predefined.CleanViewStateModel
import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState
import me.aartikov.sesame.property.state

abstract class BaseStatefullViewModel : BaseViewModel() {

    internal val statesMap = mutableMapOf<StateVariant, ViewState>()

    var state by state<ViewState>(CleanViewStateModel())
        private set

    open fun createState(variant: StateVariant): ViewState =
        throw NotImplementedError("You're should override createState function")


    @Suppress("UNCHECKED_CAST")
    open fun <T : ViewState> getState(variant: StateVariant): T {
        val state = if (statesMap.containsKey(variant))
            statesMap[variant] as T
        else
            createState(variant) as T
        return saveState(state)
    }

    @Suppress("UNCHECKED_CAST")
    open fun <T : ViewState> getExistState(variant: StateVariant): T? {
        return if (statesMap.containsKey(variant))
            statesMap[variant] as T
        else
            null
    }


    fun showState(state: StateVariant) {
        if (statesMap.containsKey(state))
            this.state = statesMap[state]!!
        else throw IllegalStateException("State ${state.state} not initialized")
    }

    fun <T : ViewState> setState(
        state: StateVariant,
        block: (T.() -> Unit)? = null
    ): T {
        val savedState = saveState<T>(getState(state))
        block?.invoke(savedState)
        applyState(savedState)
        return savedState
    }

    protected fun <T : ViewState> saveState(viewState: T): T {
        statesMap.putIfAbsent(viewState.state, viewState)
        return viewState
    }

    private fun <T : ViewState> applyState(viewState: T): T {
        this.state = viewState
        return viewState
    }

    @MainThread
    protected fun updateState(block: ViewState.() -> ViewState) {
        state = block.invoke(state)
    }

    @MainThread
    fun stateApply(block: ViewState.() -> Unit) {
        @Suppress("UNCHECKED_CAST")
        state.apply(block)
    }
}
