package ru.kirillashikhmin.personalorganiser

import com.digexco.arch.BaseApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : BaseApplication() {
  //  private lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        Settings.init(applicationContext)
       /* db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "game"
        ).fallbackToDestructiveMigration().build()
*/
    }
}
