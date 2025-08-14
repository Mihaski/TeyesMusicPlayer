package com.nuclearcode.teyesmusicplayer.di

import com.nuclearcode.teyesmusicplayer.utility.AudioPlaybackService
import com.nuclearcode.teyesmusicplayer.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectMainActivity(activity: MainActivity)
    fun injectService(service: AudioPlaybackService)
}