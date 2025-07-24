package com.nuclearcode.teyesmusicplayer.di

import com.nuclearcode.teyesmusicplayer.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ModuleCore::class])
interface MainActivityComponent {
    fun injectMainActivity(activity: MainActivity)
}