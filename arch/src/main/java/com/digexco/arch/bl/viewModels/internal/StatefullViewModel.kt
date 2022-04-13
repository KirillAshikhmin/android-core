package com.digexco.arch.bl.viewModels.internal

import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState

abstract class StatefullViewModel<Normal : ViewState> : BaseStatefullViewModel() {

    val normalState get() = getState<Normal>(StateVariant.Normal)

    abstract fun createNormalState(): Normal

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewState> getState(variant: StateVariant): T {
        val state = if (statesMap.containsKey(variant)) {
            this.statesMap[variant] as T
        } else {
            when (variant) {
                StateVariant.Normal -> createNormalState() as T
                else -> createState(variant) as T
            }
        }
        return saveState(state)
    }

    fun setNormalState(block: (Normal.() -> Unit)? = null) =
        setState(StateVariant.Normal, block)
}
