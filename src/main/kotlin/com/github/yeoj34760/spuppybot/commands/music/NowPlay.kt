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
        return if (36000 <= time / 1000)
            SimpleDateFormat("h시 m분 s초").format(Date(time))
        else if (60 <= time / 1000)
            SimpleDateFormat("m분 s초").format(Date(time))
        else
            SimpleDateFormat("s초").format(Date(time))
    }
}