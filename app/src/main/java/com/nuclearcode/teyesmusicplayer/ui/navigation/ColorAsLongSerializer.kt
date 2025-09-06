package com.nuclearcode.teyesmusicplayer.ui.navigation

import androidx.compose.ui.graphics.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object ColorAsLongSerializer : KSerializer<Color> { //todo delete
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Color", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.value.toLong()) // Color.value = ULong
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeLong().toULong())
    }
}
