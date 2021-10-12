package com.digexco.arch.ui.viewState.predefined

import androidx.lifecycle.LifecycleOwner
import com.digexco.arch.bl.viewModels.internal.states.predefined.ErrorViewState
import com.digexco.arch.databinding.StateErrorBinding
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyObserver

class PredefinedStateError(
    private val viewLifecycleOwner: LifecycleOwner,
    viewState: ErrorViewState,
    viewBinding: StateErrorBinding
) :
    PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    init {
        viewState::errorText bind { viewBinding.errorText.localizedText = it }
        viewBinding.retryButton.setOnClickListener {
            viewState.retryCommand()
        }
    }
}
