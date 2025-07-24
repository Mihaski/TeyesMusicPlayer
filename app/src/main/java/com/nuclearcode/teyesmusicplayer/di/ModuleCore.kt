package com.nuclearcode.teyesmusicplayer.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ModuleCore(private val context: Context) {
    @Provides
    fun provideContext() = context
}