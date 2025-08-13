package com.nuclearcode.teyesmusicplayer.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.content.Context

@Module
class AppModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = appContext
}