package com.nuclearcode.teyesmusicplayer.di

import androidx.media3.common.util.UnstableApi
import com.nuclearcode.teyesmusicplayer.utility.AudioPlaybackService
import com.nuclearcode.teyesmusicplayer.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@UnstableApi
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectMainActivity(activity: MainActivity)
    fun injectService(service: AudioPlaybackService)
}