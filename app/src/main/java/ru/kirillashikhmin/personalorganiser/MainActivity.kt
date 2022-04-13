package ru.kirillashikhmin.personalorganiser

import com.digexco.arch.BaseActivity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    override lateinit var navigatorHolder: NavigatorHolder
    @Inject
    override lateinit var router: Router

    override fun getInitScreen() = Screens.Calendar()

}
