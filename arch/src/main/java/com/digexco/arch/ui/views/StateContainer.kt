package com.digexco.arch.ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ViewFlipper
import androidx.annotation.AnimRes
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import androidx.viewbinding.ViewBinding
import com.digexco.arch.ui.viewState.IStateView
import com.digexco.arch.ui.viewState.StateVariant
import com.digexco.arch.ui.viewState.ViewState


abstract class StateProperty(
    open val state: StateVariant
)

class SynchronousStateProperty(
    state: StateVariant,
    val creator: (LayoutInflater) -> ViewBinding
) :
    StateProperty(state)

class AsynchronousStateProperty(
    state: StateVariant,
    @LayoutRes val layout: Int,
    val creator: (View) -> ViewBinding
) :
    StateProperty(state)

@Suppress("unused", "MemberVisibilityCanBePrivate")
class StateContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : ViewFlipper(context, attrs) {

    private var initStateViewMap: Map<StateVariant, ((ViewState, ViewBinding) -> IStateView)?>? = null

    var animationFadeId: Animation? = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    var animationFadeOut: Animation? =
        AnimationUtils.loadAnimation(context, android.R.anim.fade_out)

    private var stateCreatedCallback: IStateCreated? = null
    private var beforeStateCreatedCallback: IBeforeStateCreated? = null
    private var animationListener: IStateAnimationListener? = null

    private val states = mutableMapOf<StateVariant, StateProperty>()

    var currentState: StateVariant? = null
        private set

    init {
        inAnimation = animationFadeId
        outAnimation = animationFadeOut
    }

    operator fun invoke(block: StateContainer.() -> Unit) = run {
        block()
    }

    data class StatesData(val variableId: Int, val data: Any)

    fun addState(
        state: StateVariant,
        vbFactory: (LayoutInflater) -> ViewBinding,
    ) {
        states[state] = SynchronousStateProperty(state, vbFactory)
    }

    fun addAsyncState(
        state: StateVariant,
        @LayoutRes layoutRes: Int,
        vbFactory: (View) -> ViewBinding,
    ) {
        states[state] =
            AsynchronousStateProperty(state, layoutRes, vbFactory)
    }


    fun setState(viewState: ViewState) {
        val state = viewState.state
        if (currentState == state) return

        val stateProperty = states[state]
            ?: throw IndexOutOfBoundsException("Not found state for $state")
        val oldState = currentState
        currentState = state
        if (stateProperty is SynchronousStateProperty)
            setStateSync(stateProperty, viewState, oldState)
        else if (stateProperty is AsynchronousStateProperty)
            setStateAsync(stateProperty, viewState, oldState)

    }

    private fun setStateSync(
        property: SynchronousStateProperty,
        viewState: ViewState,
        oldState: StateVariant?
    ) {
        val state = property.state
        if (currentState != state) return

        val viewBinding = getStateBinding(property)
        initStateViewMap?.get(state)?.invoke(viewState, viewBinding)
        beforeStateCreatedCallback?.onBeforeStateCreated(viewState, viewBinding)
        showState(viewBinding, property, oldState)
        stateCreatedCallback?.onStateCreated(viewState, viewBinding)
    }

    private fun setStateAsync(
        property: AsynchronousStateProperty,
        viewState: ViewState,
        oldState: StateVariant?
    ) {
        val state = property.state
        if (currentState != state) return

        getStateBindingAsync(property) { viewBinding ->
            if (currentState != state) return@getStateBindingAsync

            initStateViewMap?.get(state)?.invoke(viewState, viewBinding)
            beforeStateCreatedCallback?.onBeforeStateCreated(viewState, viewBinding)
            showState(viewBinding, property, oldState)
            stateCreatedCallback?.onStateCreated(viewState, viewBinding)
        }
    }

    private fun getStateBinding(property: SynchronousStateProperty): ViewBinding {
        val inflater = LayoutInflater.from(context)
        return property.creator(inflater)
    }

    private fun getStateBindingAsync(
        property: AsynchronousStateProperty,
        viewCallback: (ViewBinding) -> Unit
    ) {
        AsyncLayoutInflater(context).inflate(property.layout, null) { view, _, _ ->
            val viewBinding = property.creator(view)
            viewCallback(viewBinding)
        }
    }

    fun subscribeStateCreated(stateCreatedCallback: IStateCreated) {
        this.stateCreatedCallback = stateCreatedCallback
    }

    fun subscribeBeforeStateCreated(beforeStateCreatedCallback: IBeforeStateCreated) {
        this.beforeStateCreatedCallback = beforeStateCreatedCallback
    }

    fun unsubscribeStateCreated() {
        stateCreatedCallback = null
    }

    fun unsubscribeBeforeStateCreated() {
        beforeStateCreatedCallback = null
    }

    fun subscribeStateAnimationListener(animationListener: IStateAnimationListener) {
        this.animationListener = animationListener
    }

    fun unsubscribeStateAnimationListener() {
        animationListener = null
    }

    fun initStateView(
        normal: ((ViewState, ViewBinding) -> IStateView)? = null,
        error: ((ViewState, ViewBinding) -> IStateView)? = null,
        loading: ((ViewState, ViewBinding) -> IStateView)? = null,
        noData: ((ViewState, ViewBinding) -> IStateView)? = null,
        noInternet: ((ViewState, ViewBinding) -> IStateView)? = null,
        extra: ((ViewState, ViewBinding) -> IStateView)? = null,
        empty: ((ViewState, ViewBinding) -> IStateView)? = null,
        clean: ((ViewState, ViewBinding) -> IStateView)? = null,
        vararg anotherStateViews : Pair<StateVariant, ((ViewState, ViewBinding) -> IStateView)>
    ) {
        val initStateViewMap = mutableMapOf(
            StateVariant.Loading to loading,
            StateVariant.Error to error,
            StateVariant.Normal to normal,
            StateVariant.NoData to noData,
            StateVariant.NoInternet to noInternet,
            StateVariant.Extra to extra,
            StateVariant.Empty to empty,
            StateVariant.Clean to clean,
        )
        if (anotherStateViews.isNotEmpty())
            initStateViewMap.putAll(anotherStateViews)

        this.initStateViewMap = initStateViewMap.toMap()
    }

    private fun showState(
        viewBinding: ViewBinding,
        property: StateProperty,
        oldState: StateVariant?
    ) {
        inAnimation = null
        outAnimation = null

        for (i in 0 until childCount - 1)
            removeViewAt(0)

        val hasState =
            oldState != null && childCount != 0 //True if any state showed. No need show animation for first state
        if (hasState) {
            val animation =
                animationListener?.getAnimation(oldState!!, property.state) ?: AnimationPair()

            val animationIn = if (animation.animationIn > 0)
                AnimationUtils.loadAnimation(context, animation.animationIn)
            else
                animationFadeId

            val animationOut = if (animation.animationOut > 0)
                AnimationUtils.loadAnimation(context, animation.animationOut)
            else
                animationFadeOut

            inAnimation = animationIn
            outAnimation = animationOut
        }

        this.addView(viewBinding.root)
        showNext()
    }
}

data class AnimationPair(@AnimRes val animationIn: Int, @AnimRes val animationOut: Int) {
    constructor() : this(-1, -1)
}

fun interface IBeforeStateCreated {
    fun onBeforeStateCreated(viewState: ViewState, viewBinding: ViewBinding)
}

fun interface IStateCreated {
    fun onStateCreated(viewState: ViewState, viewBinding: ViewBinding)
}

fun interface IStateAnimationListener {
    fun getAnimation(fromState: StateVariant, toState: StateVariant): AnimationPair
}
