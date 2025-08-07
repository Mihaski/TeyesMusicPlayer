package com.nuclearcode.teyesmusicplayer.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AudioFile(
    val id: Int,
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long,
    val albumId: Long,
    val embeddedArt: ByteArray? = null
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioFile

        if (id != other.id) return false
        if (duration != other.duration) return false
        if (albumId != other.albumId) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (path != other.path) return false
        if (!embeddedArt.contentEquals(other.embeddedArt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + duration.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (embeddedArt?.contentHashCode() ?: 0)
        return result
    }

}
