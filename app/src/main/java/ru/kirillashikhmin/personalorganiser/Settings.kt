package ru.kirillashikhmin.personalorganiser

import com.digexco.arch.services.SettingsCore
import com.digexco.arch.services.preferenceObject

object Settings : SettingsCore() {
    override fun cleanAll() {}

    var sets by preferenceObject(listOf<String>())
    var assetsVersion by preferenceObject(0)
    var gameActive by preferenceObject(false)
    var answers by preferenceObject(listOf<String>())
    var round by preferenceObject(1)
    var currentQuestionId by preferenceObject(-1)
    var purchasedSku by preferenceObject(listOf<String>())
    var subscribed by preferenceObject(false)
    var purchasesLoaded by preferenceObject(false)
}
