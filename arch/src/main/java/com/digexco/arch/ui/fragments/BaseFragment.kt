package com.digexco.arch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.digexco.arch.helpers.onEachInScope
import com.digexco.arch.ArchConsts
import com.digexco.arch.R
import com.digexco.arch.bl.viewModels.internal.BaseViewModel
import com.digexco.arch.helpers.BackPressedInterceptor
import com.digexco.arch.helpers.toMap
import com.digexco.arch.helpers.tryGetValue
import com.digexco.arch.ui.dialogs.DialogResolver
import kotlinx.coroutines.flow.Flow
import me.aartikov.sesame.property.PropertyObserver


abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout), PropertyObserver,
    BackPressedInterceptor {

    abstract val binding: ViewBinding
    abstract val vm: BaseViewModel
    override val propertyObserverLifecycleOwner: LifecycleOwner get() = viewLifecycleOwner
    private  var dialogService : DialogResolver? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argumentsMap = arguments?.toMap()
        argumentsMap?.let {
            vm.navigationParams =
                it.tryGetValue<Bundle>(ArchConsts.navigation_properties)?.toMap() ?: mapOf()
            vm.resultKey = it.tryGetValue<String>(ArchConsts.navigation_result_key)
        }
        vm.onViewCreated()
        vm.dialog.showAlert bind {
            if (dialogService==null) dialogService = DialogResolver(requireContext(), childFragmentManager)
            dialogService?.show(it.first, it.second)
        }
    }

    override fun onBackPressed(): Boolean = vm.close()

    override fun onDestroyView() {
        super.onDestroyView()
        dialogService = null
        vm.viewClosed()
    }


    fun Toolbar.addToolbarRightCloseButton() {
        this.inflateMenu(R.menu.toolbar_close)
        this.setOnMenuItemClickListener {
            if (it.itemId == R.id.close) vm.close() else
                true
        }
    }

    fun Toolbar.addToolbarCloseButton() {
        this.setNavigationIcon(R.drawable.ic_close)
        this.setNavigationOnClickListener { vm.close() }
    }

    fun Toolbar.addToolbarBackButton() {
        this.setNavigationIcon(R.drawable.ic_back)
        this.setNavigationOnClickListener { vm.close() }
    }

    fun <T> Flow<T>.onEachInLifecycleScope(action: suspend (T) -> Unit) {
        this.onEachInScope(lifecycleScope, action)
    }


}
