package com.digexco.arch.services

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.digexco.arch.ArchConsts
import com.digexco.arch.helpers.ArchFragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

open class ArchFragmentScreen @JvmOverloads constructor(
    private val key: String,
    private val arguments: Bundle? = null,
    private val resultKey: String? = null
) : FragmentScreen {
    override val screenKey: String get() = key
    override fun createFragment(factory: FragmentFactory): Fragment {
        val fragment = (factory as ArchFragmentFactory).instantiate(key)
        val args = Bundle()
        args.putString(ArchConsts.navigation_name, key)
        if (arguments != null) args.putBundle(ArchConsts.navigation_properties, arguments)
        if (!resultKey.isNullOrEmpty()) args.putString(ArchConsts.navigation_result_key, resultKey)
        fragment.arguments = args
        return fragment
    }
}
