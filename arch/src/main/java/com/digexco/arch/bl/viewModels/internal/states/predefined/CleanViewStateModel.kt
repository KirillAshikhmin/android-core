package com.digexco.arch.bl.viewModels.internal.states.predefined

import com.digexco.arch.ui.viewState.ViewState
import com.digexco.arch.ui.viewState.StateVariant

class CleanViewStateModel : ViewState() {
    override var state: StateVariant = StateVariant.Clean
}
