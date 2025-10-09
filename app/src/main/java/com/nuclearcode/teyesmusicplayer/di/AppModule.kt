package com.nuclearcode.teyesmusicplayer.di

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.content.Context
import androidx.room.Room
import com.nuclearcode.teyesmusicplayer.rpl.AppDatabase
import com.nuclearcode.teyesmusicplayer.rpl.FavoriteDao

@Module
class AppModule(private val appContext: Context) {
    @Provides
    @Singleton
    fun provideContext(): Context = appContext
    @Provides
    @Singleton
    fun provideAppDatabase(appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "player.db"
        ).build()
    }
    @Provides
    fun provideFavoriteDao(db: AppDatabase): FavoriteDao = db.favoriteDao()
}