package com.nuclearcode.teyesmusicplayer.di

import com.nuclearcode.teyesmusicplayer.AudioPlaybackService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleCore::class])
interface AudioPlaybackServiceComponent {

    fun injectService(service: AudioPlaybackService)
}