package ru.kirillashikhmin.personalorganiser

import com.digexco.arch.services.ArchFragmentScreen
import com.digexco.arch.ui.ScreensCore

@Suppress("FunctionName")
object Screens : ScreensCore() {

    fun Calendar(): ArchFragmentScreen =
        screenByName(Screens::Calendar.name)

    object Arguments {
        const val CAN_CONTINUE = "date"
    }
}
