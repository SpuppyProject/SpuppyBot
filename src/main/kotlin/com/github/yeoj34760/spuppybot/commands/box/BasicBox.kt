package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.userbox.UserBoxInfo
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.*

abstract class BasicBox {
   abstract fun execute(event: MessageReceivedEvent)
    protected fun sendBox(event: MessageReceivedEvent, track: AudioTrack) {
        val info = UserBoxInfo(
                track.info.title,
                track.info.length,
                track.info.uri,
                event.author.name,
                track.info.author
        )
        val infoBase64 = Base64.getEncoder().encodeToString(Json.encodeToString(info).toByteArray())
        SpuppyDBController.addUserBox(event.author.idLong, infoBase64)
    }

    protected fun fromArgs(event: MessageReceivedEvent, commands: List<String>): String? {
        try {
            commands.forEach {
                if (event.message.contentRaw.startsWith(Settings.PREFIX + it)) {
                    return event.message.contentRaw.substring(Settings.PREFIX.length + it.length+1)
                }
            }
        }
        catch (e: Exception) {
            return null
        }
        return null
    }
}