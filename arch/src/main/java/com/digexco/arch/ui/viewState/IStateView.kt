package com.digexco.arch.ui.viewState

import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

interface IStateView {
    val viewLifecycleOwner: LifecycleOwner
    val viewState: ViewState
    val viewBinding: ViewBinding
}
