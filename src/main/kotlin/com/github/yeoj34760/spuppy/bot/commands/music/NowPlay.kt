package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.enhance.EmbedColor
import com.github.yeoj34760.spuppy.utilities.enhance.TimeFormat
import com.github.yeoj34760.spuppy.utilities.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.User

object NowPlay : Command(name = "nowPlay", aliases = Bot.commands["nowplay"] ?: error("umm..")) {
    private const val BAR_LENGTH = 30
    override suspend fun execute(event: CommandEvent) {
        val control = PlayerUtil.loadPlayerControl(event) ?: return
        val first = control.playingTrack()!!

        event.send {
            author {
                name = event.author.name
                iconUrl = event.author.avatarUrl ?: event.author.defaultAvatarUrl
            }

            color = EmbedColor.YELLOW.rgb

            description = """
                **${first.info.title}**
                ${if (first.info.isStream) "🔴 stream" else createBar(first)}
            """.trimIndent()

            thumbnail = PlayerUtil.youtubeToThumbnail(first.info.identifier)


            when (first.info.isStream) {
                false -> addField {
                    name = "video length"
                    value = "[${TimeFormat.simple(first.duration)}]"
                    inline = true
                }

                true -> addField {
                    name = "들은 시간"
                    value = "[${TimeFormat.simple(first.position)}]"
                    inline = true
                }
            }

            addField {
                name = "만든이"
                value = first.info.author
                inline = true
            }

            addField {
                name = "신청자"
                value = (first.userData as User).name
                inline = true
            }
        }
    }

    private fun createBar(track: AudioTrack): String {
        val length = (track.position.toDouble() / track.duration * BAR_LENGTH).toInt()
        val barContent = StringBuffer("`├")

        for (i in 0 until BAR_LENGTH) {
            barContent.append("─")
            if (i == length)
                barContent.append("[${TimeFormat.simple(track.position)}]")
        }
        barContent.append("┤`")

        return barContent.toString()
    }
}