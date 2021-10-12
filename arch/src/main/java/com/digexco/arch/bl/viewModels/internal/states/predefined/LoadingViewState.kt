package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.R
import com.digexco.arch.ui.viewState.PropertyViewState
import com.digexco.arch.ui.viewState.StateVariant
import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.localizedstring.LocalizedString
import me.aartikov.sesame.property.state

open class LoadingViewState(viewModelScope: CoroutineScope) : PropertyViewState(viewModelScope, StateVariant.Loading) {
    var loadingText by state<LocalizedString>(LocalizedString.resource(R.string.loading))
}


