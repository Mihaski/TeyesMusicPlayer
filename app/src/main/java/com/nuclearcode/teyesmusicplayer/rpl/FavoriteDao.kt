package com.nuclearcode.teyesmusicplayer.rpl

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE trackId = :id)")
    suspend fun isFavorite(id: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(fav: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(fav: FavoriteEntity)

    @Query("SELECT trackId FROM favorites")
    fun getAllFavorites(): Flow<List<Long>>
}