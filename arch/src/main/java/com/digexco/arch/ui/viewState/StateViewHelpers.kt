package com.digexco.arch.ui.viewState

import com.digexco.arch.databinding.StateErrorBinding
import com.digexco.arch.databinding.StateLoadingBinding
import com.digexco.arch.databinding.StateNoDataBinding
import com.digexco.arch.ui.views.StateContainer

fun StateContainer.addLoadingErrorNoDataState() {
    addLoadingState()
    addErrorState()
    addNoDataState()
}

fun StateContainer.addLoadingState() {
    addState(StateVariant.Loading) { StateLoadingBinding.inflate(it) }
}

fun StateContainer.addErrorState() {
    addState(StateVariant.Error) { StateErrorBinding.inflate(it) }
}

fun StateContainer.addNoDataState() {
    addState(StateVariant.NoData) { StateNoDataBinding.inflate(it) }
}


fun StateContainer.addPredefinedStates(
    loading: Boolean = false,
    error: Boolean = false,
    noData: Boolean = false
) {
    if (loading) addLoadingState()
    if (error) addErrorState()
    if (noData) addNoDataState()
}
