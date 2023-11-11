package ua.com.andromeda.amphibians

import android.app.Application
import ua.com.andromeda.amphibians.data.AppContainer
import ua.com.andromeda.amphibians.data.DefaultAppContainer

class MyApplication : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}