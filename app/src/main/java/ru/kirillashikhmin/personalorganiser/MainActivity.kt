package ru.kirillashikhmin.personalorganiser

import com.digexco.arch.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun getInitScreen() = Screens.Calendar()

}
