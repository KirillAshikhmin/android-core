package com.digexco.arch.ui.viewState.predefined

import androidx.lifecycle.LifecycleOwner
import com.digexco.arch.bl.viewModels.internal.states.predefined.LoadingViewStateModel
import com.digexco.arch.databinding.StateLoadingBinding
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyObserver

class PredefinedLoadingStateView(
    private val viewLifecycleOwner: LifecycleOwner,
    private val viewStateModel: LoadingViewStateModel,
    private val viewBinding: StateLoadingBinding
) :
    PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    init {
        viewStateModel::loadingText bind { viewBinding.loadingText.localizedText = it }
    }
}
