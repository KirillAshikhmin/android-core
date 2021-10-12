package com.digexco.arch.bl.viewModels.internal

import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState

abstract class ThreeStatefullViewModel<Normal : ViewState, Loading : ViewState, Error : ViewState> :
    StatefullViewModel<Normal>() {

    val loadingState get() = getState<Loading>(StateVariant.Loading)
    val errorState get() = getState<Error>(StateVariant.Error)


    abstract fun createLoadingState(): Loading
    abstract fun createErrorState(): Error


    fun setLoadingState(block: (Loading.() -> Unit)? = null) =
        setState(StateVariant.Loading, block)

    fun setErrorState(block: (Error.() -> Unit)? = null) =
        setState(StateVariant.Error, block)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewState> getState(variant: StateVariant): T {
        val state = if (statesMap.containsKey(variant)) {
            this.statesMap[variant] as T
        } else {
            when (variant) {
                StateVariant.Loading -> createLoadingState() as T
                StateVariant.Error -> createErrorState() as T
                else -> super.getState(variant)
            }
        }
        return saveState(state)
    }

}
