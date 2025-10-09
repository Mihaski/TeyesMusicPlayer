package com.nuclearcode.teyesmusicplayer.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class AudioFile(
    val contentUri: String,
    val osEmbeddedIdLong: Long,
    val appId: Int,
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

        if (osEmbeddedIdLong != other.osEmbeddedIdLong) return false
        if (appId != other.appId) return false
        if (duration != other.duration) return false
        if (albumId != other.albumId) return false
        if (contentUri != other.contentUri) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (path != other.path) return false
        if (!embeddedArt.contentEquals(other.embeddedArt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = osEmbeddedIdLong.hashCode()
        result = 31 * result + appId
        result = 31 * result + duration.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + contentUri.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (embeddedArt?.contentHashCode() ?: 0)
        return result
    }
}