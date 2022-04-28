package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.R
import com.digexco.arch.ui.viewState.PropertyViewState
import com.digexco.arch.ui.viewState.StateVariant
import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.localizedstring.LocalizedString
import me.aartikov.sesame.property.state

open class NoDataViewStateModel(viewModelScope: CoroutineScope) :
    PropertyViewState(viewModelScope, StateVariant.NoData) {
    var noDataText by state<LocalizedString>(LocalizedString.resource(R.string.no_data))
}
