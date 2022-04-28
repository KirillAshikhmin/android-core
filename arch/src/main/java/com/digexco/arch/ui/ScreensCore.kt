package com.digexco.arch.ui

import android.content.Context
import android.content.Intent
import com.digexco.arch.services.ArchFragmentScreen
import com.digexco.arch.helpers.toBundle
import com.github.terrakok.cicerone.androidx.ActivityScreen
import kotlin.properties.ReadOnlyProperty

@Suppress("unused")
open class ScreensCore {

    var getActivityIntentByName: ((Context, String) -> Intent) = { _, _ -> throw NotImplementedError() }

    fun getActivityScreen(name: String, intentAction : (Intent)-> Unit) =
        ActivityScreen { getActivityIntentByName(it, name).apply { intentAction(this) } }

    fun screenByName(name: String, arguments: Map<String, Any>? = null, resultKey: String? = null, namespace : String? = null) =
        ArchFragmentScreen(name, arguments?.toBundle(), resultKey, namespace)

    private fun screen(arguments: Map<String, Any>? = null, resultKey: String? = null, namespace : String? = null) =
        ReadOnlyProperty<Any, ArchFragmentScreen> { _, property ->
            screenByName(
                property.name,
                arguments,
                resultKey,
                namespace
            )
        }
}
