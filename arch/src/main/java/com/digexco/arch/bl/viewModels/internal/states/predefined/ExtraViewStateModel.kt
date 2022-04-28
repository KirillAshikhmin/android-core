package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState

open class ExtraViewStateModel : ViewState() {
    override var state: StateVariant = StateVariant.Extra
}
