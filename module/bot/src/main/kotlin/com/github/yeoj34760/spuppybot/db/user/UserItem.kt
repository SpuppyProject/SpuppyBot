package com.github.yeoj34760.spuppybot.db.user


import com.github.yeoj34760.spuppybot.db.DateTimeSerializer
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joda.time.DateTime


@Serializable
data class UserItem(
        val name: String,
        var count: Int,
        @Serializable(with = DateTimeSerializer::class)
        var timestamp: DateTime
)

