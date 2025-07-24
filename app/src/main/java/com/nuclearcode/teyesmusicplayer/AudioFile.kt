package com.nuclearcode.teyesmusicplayer

data class AudioFile(
    val title: String,
    val artist: String,
    val path: String,
    val duration: Long,
    val albumId: Long,
    val embeddedArt: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioFile

        if (duration != other.duration) return false
        if (albumId != other.albumId) return false
        if (title != other.title) return false
        if (artist != other.artist) return false
        if (path != other.path) return false
        if (!embeddedArt.contentEquals(other.embeddedArt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = duration.hashCode()
        result = 31 * result + albumId.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + artist.hashCode()
        result = 31 * result + path.hashCode()
        result = 31 * result + (embeddedArt?.contentHashCode() ?: 0)
        return result
    }
}
