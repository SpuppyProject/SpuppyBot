package com.github.yeoj34760.spuppybot.commands.music


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.other.Util
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration


@CommandSettings(name = "nowplay")
object NowPlay : Command() {

    override fun execute(event: CommandEvent) {
        if (GuildManager.playerControls[event.guild.idLong] == null || !GuildManager.playerControls[event.guild.idLong]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생중인 음악이 없네요.").queue()
            return
        }

        val playerControl = GuildManager.playerControls[event.guild.idLong]
        val playingTrack = playerControl!!.playingTrack()
        val timeMax: String
        val timeRemain: String
        if (playingTrack.info.isStream) {
            timeRemain = "LIVE"
            timeMax = "LIVE"
        } else {
            timeRemain = timeFormat(playingTrack.duration - playingTrack.position)
            timeMax = timeFormat(playingTrack.duration)
        }
        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setDescription("[${playingTrack.info.title}](${playingTrack.info.uri})")
                .setThumbnail(Util.youtubeToThumbnail(playingTrack.info.identifier))
                .addField("최대 길이", timeMax, true)
                .addField("들은 시간", timeFormat(playingTrack.position), true)
                .addField("남은 시간", timeRemain, true)
                .addField("만든이", playingTrack.info.author, true)
                .addField("무한 루프 여부", playerControl.isLooped.toString(), true)
                .setFooter("신청자: ${(playingTrack.userData as User).asTag}")
                .setColor(DiscordColor.BLUE)
                .build()
        event.channel.sendMessage(embed).queue()
    }

    private fun timeFormat(time: Long): String {
        val secondTime = time / 1000
        val day = secondTime / 86400
        val hour = (secondTime % (86400)) / 3600
        val minute = (secondTime % (3600)) / 60
        val second = (secondTime % (60))

        return if (86400 <= secondTime)
            "${day}일 ${hour}시 ${minute}분 ${second}초"
        else if (3600 <= secondTime)
            "${hour}시 ${minute}분 ${second}초"
        else if (60 <= secondTime)
            "${minute}분 ${second}초"
        else
            "${second}초"
    }
}