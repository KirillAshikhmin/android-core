package com.digexco.arch.ui.viewState.predefined

import androidx.lifecycle.LifecycleOwner
import com.digexco.arch.bl.viewModels.internal.states.predefined.LoadingViewState
import com.digexco.arch.databinding.StateLoadingBinding
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyObserver

class PredefinedStateLoading(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewState: LoadingViewState,
    private val viewBinding: StateLoadingBinding
) :
    PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    init {
        viewState::loadingText bind { viewBinding.loadingText.localizedText = it }
    }
}
