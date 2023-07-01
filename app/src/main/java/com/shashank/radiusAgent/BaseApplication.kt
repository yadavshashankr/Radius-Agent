package com.shashank.radiusAgent

import android.app.Application
import com.shashank.radiusAgent.utils.Prefs
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.init(this)
    }
}

