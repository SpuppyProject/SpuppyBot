package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.TimeFormat
import com.github.yeoj34760.spuppybot.music.Util
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User


@CommandSettings(name = "nowplay")
object NowPlay : Command() {

    override fun execute(event: CommandEvent) {
        if (GuildManager.playerControls[event.guild.idLong] == null || !GuildManager.playerControls[event.guild.idLong]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생중인 음악이 없어요!").queue()
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
            timeRemain = TimeFormat.hangul(playingTrack.duration - playingTrack.position)
            timeMax = TimeFormat.simple(playingTrack.duration)
        }
        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setDescription("[${playingTrack.info.title}](${playingTrack.info.uri})\n[${TimeFormat.simple(playingTrack.position)}/${timeMax}]")
                .setThumbnail(Util.youtubeToThumbnail(playingTrack.info.identifier))
//                .addField("들은 시간/음악 길이", "[${simpleTimeFormat(playingTrack.position)}/${timeMax}]", true)
                .addField("남은 시간", timeRemain, true)
                .addField("만든이", playingTrack.info.author, true)
                .addField("셔플 여부", playerControl.isLooped.toString(), true)
                .setFooter("신청자: ${(playingTrack.userData as User).asTag}")
                .setColor(DiscordColor.BLUE)
                .build()
        event.channel.sendMessage(embed).queue()
    }

}