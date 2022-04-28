package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.ui.viewState.PropertyViewState
import com.digexco.arch.ui.viewState.StateVariant
import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.property.state

class GenericNormalViewStateModel<T : Any>(val viewModelScope: CoroutineScope, defaultValue: T) :
    PropertyViewState(viewModelScope, StateVariant.Normal) {
    var data by state<T>(defaultValue)
}
