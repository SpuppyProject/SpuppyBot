package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.DiscordColor
import com.github.yeoj34760.spuppybot.other.Util
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.User

object NowPlay : Command() {
    init {
        name = "NowPlay"
        aliases = arrayOf("now", "nowplay", "np", "ㅞ", "ㅜㅐㅈ")
    }
    override fun execute(event: CommandEvent) {
        if (GuildManager[event.guild.idLong] == null || !GuildManager[event.guild.idLong]!!.isPlayed()) {
            event.reply("현재 재생중인 음악이 없네요.")
            return
        }

        var trackScheduler = GuildManager.get(event.guild.idLong)
        var playingTrack = trackScheduler!!.playingTrack()
        var embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setDescription("[${playingTrack.info.title}](${playingTrack.info.uri}) 재생 중")
                .setThumbnail(Util.youtubeToThumbnail(playingTrack.info.identifier))
                .addField("만든이", playingTrack.info.author, true)
                .addField("신청자", (playingTrack.userData as User).name, true)
                .addField("최대 길이", "${playingTrack.duration / 1000}초", true)
                .addField("들은 시간", "${playingTrack.position / 1000}초", true)
                .addField("남은 시간", "${ (playingTrack.duration-playingTrack.position) / 1000}초", true)
                .setColor(DiscordColor.BLUE)
                .build()
        event.reply(embed)
    }
}