package com.digexco.arch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.digexco.arch.helpers.BackPressedInterceptor
import com.digexco.common.Logger
import com.github.terrakok.cicerone.*
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigator = object : AppNavigator(this, R.id.fragment_container) {
            override fun applyCommand(command: Command) {
                super.applyCommand(command)
                val log = when (command) {
                    is Forward -> "Forward to ${command.screen.screenKey}"
                    is Replace -> "Replace to ${command.screen.screenKey}"
                    is BackTo -> "Back to ${command.screen?.screenKey ?: "Unspecified"}"
                    is Back -> "Back"
                    else -> command.toString()
                }
             //   hideKeyboard()
                Logger.i("Navigation", log)
            }

            override fun setupFragmentTransaction(
                screen: FragmentScreen,
                fragmentTransaction: FragmentTransaction,
                currentFragment: Fragment?,
                nextFragment: Fragment
            ) {
                super.setupFragmentTransaction(
                    screen,
                    fragmentTransaction,
                    currentFragment,
                    nextFragment
                )
                fragmentTransaction.setReorderingAllowed(true)
                if (currentFragment == null) return
                fragmentTransaction.setCustomAnimations(
                    R.anim.slide_in_left,
                    R.anim.slide_out_left,
                    R.anim.slide_in_right,
                    R.anim.slide_out_right
                )
            }
        }
        if (savedInstanceState == null)
            navigator.applyCommands(arrayOf(Replace(getInitScreen())))

    }

    abstract fun getInitScreen(): Screen


    override fun onBackPressed() {
        val fragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as? BackPressedInterceptor
        if (fragment == null || !fragment.onBackPressed()) super.onBackPressed()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }
    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

}
