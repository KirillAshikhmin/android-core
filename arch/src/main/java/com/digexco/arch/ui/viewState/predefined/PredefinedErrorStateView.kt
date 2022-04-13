package com.digexco.arch.ui.viewState.predefined

import androidx.lifecycle.LifecycleOwner
import com.digexco.arch.bl.viewModels.internal.states.predefined.ErrorViewStateModel
import com.digexco.arch.databinding.StateErrorBinding
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyObserver

class PredefinedErrorStateView(
    private val viewLifecycleOwner: LifecycleOwner,
    viewStateModel: ErrorViewStateModel,
    viewBinding: StateErrorBinding
) :
    PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    init {
        viewStateModel::errorText bind { viewBinding.errorText.localizedText = it }
        viewBinding.retryButton.setOnClickListener {
            viewStateModel.retryCommand()
        }
    }
}
