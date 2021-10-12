package com.digexco.arch.helpers.di

import android.content.Context
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import com.digexco.arch.helpers.di.module.NavigationModule
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class])
interface AppComponent {
    fun provideContext(): Context
    fun provideRouter(): Router

    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

    class Initializer private constructor() {
        companion object {
            fun init(context: Context): AppComponent =
                DaggerAppComponent.builder()
                    .context(context)
                    .build()
        }
    }
}
