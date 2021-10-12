package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.R
import com.digexco.arch.helpers.reverseCommand
import com.digexco.arch.ui.viewState.PropertyViewState
import com.digexco.arch.ui.viewState.StateVariant
import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.localizedstring.LocalizedString
import me.aartikov.sesame.property.state

open class ErrorViewState(viewModelScope: CoroutineScope, retryCommand: suspend () -> Unit) :
    PropertyViewState(viewModelScope, StateVariant.Error) {
    val retryCommand = reverseCommand { retryCommand() }
    var errorText by state<LocalizedString>(LocalizedString.resource(R.string.unable_load_data))
}
