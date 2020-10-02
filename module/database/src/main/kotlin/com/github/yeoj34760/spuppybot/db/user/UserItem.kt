package com.github.yeoj34760.spuppybot.db.user


import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joda.time.DateTime


@ExperimentalSerializationApi
@Serializable
data class UserItem(
        val name: String,
        var count: Int,
        @Serializable(with = DateTimeSerializer::class)
        var timestamp: DateTime
)

object DateTimeSerializer : KSerializer<DateTime> {
    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("DateTime", PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): DateTime = DateTime(decoder.decodeLong())
    override fun serialize(encoder: Encoder, value: DateTime) = encoder.encodeLong(value = value.millis)
}

