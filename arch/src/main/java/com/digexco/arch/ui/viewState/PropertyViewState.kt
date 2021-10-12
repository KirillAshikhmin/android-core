package com.digexco.arch.ui.viewState

import kotlinx.coroutines.CoroutineScope
import me.aartikov.sesame.property.PropertyHost

abstract class PropertyViewState(private val viewModelScope: CoroutineScope, override val state: StateVariant) : ViewState(),
    PropertyHost {

    override val propertyHostScope: CoroutineScope get() = viewModelScope

}

