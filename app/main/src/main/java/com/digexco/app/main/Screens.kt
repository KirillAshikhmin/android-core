package com.digexco.app.main

import android.content.Intent
import android.net.Uri
import com.digexco.arch.ui.ScreensCore
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.ActivityScreen

@Suppress("FunctionName")
object Screens : ScreensCore() {
    fun OpenLink(link: String): Screen {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(link)
        return ActivityScreen { i }
    }
}
