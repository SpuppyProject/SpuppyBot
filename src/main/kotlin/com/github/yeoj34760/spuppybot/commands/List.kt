package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.GuildManager
import com.github.yeoj34760.spuppybot.other.TrackScheduler
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed

object List : Command() {
    init {
        super.name = "list"
        super.aliases = arrayOf("list", "리스트","ㅣㅑ", "li")
    }

    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong

        if (!GuildManager.isTrackCreated(id) || !GuildManager.get(id).isPlayed) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여드리고 싶었지만 아쉽게도 아무 음악이 없네요").queue()
            return
        }
        val trackScheduler = GuildManager.get(id)
        var list: String = ""
        var i = 1
        //대기열이 없을 경우 `없음`으로 표기합니다.
        if (GuildManager.get(id).trackQueue.isNotEmpty())
           GuildManager.get(id).trackQueue.forEach {
                list += "${i++}. [${it.audioTrack.info.title}](${it.audioTrack.info.uri})\n신청자: `${it.user.name}`\n\n"
            }
        else
            list = "없음"
        val embed = EmbedBuilder()
                .setTitle("SppuppyBot")
                .setThumbnail(Util.youtubeToThumbnail(GuildManager.get(id).playingTrack().info.identifier))
                .setDescription("재생중 -> [${trackScheduler.playingTrack().info.title}](${trackScheduler.playingTrack().info.uri})\n만든이: `${trackScheduler.playingTrack().info.author}`\n신청자: `${trackScheduler.nowUser!!.name}`\n")
                .addField("남은 대기열", list, false)
                .build()
        event.channel.sendMessage(embed).queue()
    }
}