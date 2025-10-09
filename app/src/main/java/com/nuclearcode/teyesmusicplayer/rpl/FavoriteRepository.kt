package com.nuclearcode.teyesmusicplayer.rpl

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoriteRepository @Inject constructor(private val dao: FavoriteDao) {

    fun getFavorites(): Flow<List<Long>> = dao.getAllFavorites()

    suspend fun isFavorite(trackId: Long): Boolean =
        dao.isFavorite(trackId)

    suspend fun toggleFavorite(trackId: Long) {
        if (dao.isFavorite(trackId)) {
            dao.removeFavorite(FavoriteEntity(trackId))
        } else {
            dao.addFavorite(FavoriteEntity(trackId))
        }
    }
}
