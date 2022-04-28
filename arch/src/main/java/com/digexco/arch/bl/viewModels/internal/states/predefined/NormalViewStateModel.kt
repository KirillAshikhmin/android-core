package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState

class NormalViewStateModel : ViewState() {
    override var state: StateVariant = StateVariant.Normal
}
