package com.nuclearcode.teyesmusicplayer

import android.app.Application
import com.nuclearcode.teyesmusicplayer.di.AppComponent
import com.nuclearcode.teyesmusicplayer.di.AppModule
import com.nuclearcode.teyesmusicplayer.di.DaggerAppComponent

class PlayerApp : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
