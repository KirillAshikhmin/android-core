package com.digexco.arch.ui.viewState.predefined

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import com.digexco.arch.bl.viewModels.internal.states.predefined.NoDataViewStateModel
import com.digexco.arch.databinding.StateNoDataBinding
import com.digexco.arch.ui.viewState.IStateView
import com.digexco.arch.ui.viewState.ViewState
import me.aartikov.sesame.localizedstring.localizedText
import me.aartikov.sesame.property.PropertyObserver

class PredefinedNoDataStateView(
    override val viewLifecycleOwner: LifecycleOwner,
    override val viewState: ViewState,
    override val viewBinding: ViewBinding
) : IStateView,
    PropertyObserver {

    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner

    private val state = viewState as NoDataViewStateModel
    private val binding = viewBinding as StateNoDataBinding

    init {
        state::noDataText bind { binding.errorText.localizedText = it }
    }
}
