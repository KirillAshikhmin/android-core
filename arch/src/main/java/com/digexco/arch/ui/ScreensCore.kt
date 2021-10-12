package com.digexco.arch.ui

import com.digexco.arch.services.ArchFragmentScreen
import com.digexco.arch.helpers.toBundle

@Suppress("unused")
open class ScreensCore {
    fun screenByName(name: String, arguments: Map<String, Any>? = null, resultKey: String? = null) =
        ArchFragmentScreen(name, arguments?.toBundle(), resultKey)
}
