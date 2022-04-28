package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.helpers.reverseCommandValue
import com.digexco.arch.ui.viewState.PropertyViewState
import com.digexco.arch.ui.viewState.StateVariant
import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.property.state

open class ListNormalStateModel<T : Any>(val viewModelScope: CoroutineScope, defaultValue: List<T>, itemClick: suspend (T) -> Unit) :
    PropertyViewState(viewModelScope, StateVariant.Normal) {
    var data by state<List<T>>(defaultValue)
    val itemClickCommand = reverseCommandValue(itemClick)
}
