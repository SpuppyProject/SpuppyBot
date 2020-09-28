package com.github.yeoj34760.spuppybot

import com.sedmelluq.discord.lavaplayer.tools.io.MessageInput
import com.sedmelluq.discord.lavaplayer.tools.io.MessageOutput
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*

object TrackManager {
    fun base64ToTrack(base64: String): AudioTrack {
        val decode = Base64.getDecoder().decode(base64)
        val inputStream = ByteArrayInputStream(decode)
        return playerManager.decodeTrack(MessageInput(inputStream)).decodedTrack
    }

    fun trackToBase64(track: AudioTrack): String {
        val stream = ByteArrayOutputStream()
        playerManager.encodeTrack(MessageOutput(stream), track)
        val encoded = Base64.getEncoder().encode(stream.toByteArray())
        return String(encoded)
    }
}