package com.nuclearcode.teyesmusicplayer.utility

import android.content.ContentUris
import android.content.Context
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import com.nuclearcode.teyesmusicplayer.ui.AudioFile

fun getAudioFiles(context: Context): List<AudioFile> {
    val audioList = mutableListOf<AudioFile>()

    val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.ALBUM_ID
    )

    val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
    val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

    var idCounter = 0


    context.contentResolver.query(
        collection, projection, selection, null, sortOrder
    )?.use { cursor ->
        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
        val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
        val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
        val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
        val idLongColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)

        while (cursor.moveToNext()) {

            val id = cursor.getLong(idLongColumn)
            val title = cursor.getString(titleColumn)
            val artist = cursor.getString(artistColumn)
            val path = cursor.getString(pathColumn)
            val duration = cursor.getLong(durationColumn)
            val albumId = cursor.getLong(albumIdColumn)

            val embeddedArt = getEmbeddedArtwork(path)

            val contentUri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                id
            )

            audioList.add(
                AudioFile(
                    contentUri = contentUri.toString(),
                    appId = idCounter++,
                    title = title,
                    artist = artist,
                    path = path,
                    duration = duration,
                    albumId = albumId,
                    embeddedArt = embeddedArt,
                    osEmbeddedIdLong = id
                )
            )
        }
    }

    return audioList
}

fun getEmbeddedArtwork(path: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(path)
        retriever.embeddedPicture
    } catch (_: Exception) {
        null
    } finally {
        retriever.release()
    }
}