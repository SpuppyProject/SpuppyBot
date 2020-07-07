package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.GuildManager
import com.github.yeoj34760.spuppybot.other.TrackScheduler
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User

object List : Command() {
    init {
        super.name = "list"
        super.aliases = arrayOf("list", "리스트", "ㅣㅑ", "li")
    }

    val PAGE_MAX = 5
    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong

        if (!GuildManager.isTrackCreated(id) || !GuildManager.get(id).isPlayed) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여드리고 싶었지만 아쉽게도 아무 음악이 없네요").queue()
            return
        }
        val trackScheduler = GuildManager.get(id)
        val playingTrack = trackScheduler.playingTrack()
        val user = playingTrack.userData as User
        var page = if (event.args.isEmpty()) 1 else event.args.toInt()
        var tracks = GuildManager.get(id).trackQueue.toTypedArray()
        var max = if (PAGE_MAX * page > trackScheduler.trackQueue.size) trackScheduler.trackQueue.size else PAGE_MAX * page


        event.channel.sendMessage(embed(playingTrack, user, tracksList(tracks, max, page))).queue()
    }

    fun tracksList(tracks: Array<AudioTrack>, max: Int, page: Int): String {
        var temp: String = ""
        //대기열이 없을 경우 `없음`으로 표기합니다.
        if (tracks.isNotEmpty())
            for (i in page * PAGE_MAX - PAGE_MAX until max)
                temp += "${i + 1}. [${tracks[i].info.title}](${tracks[i].info.uri})\n신청자: `${(tracks[i].userData as User).name}`\n\n"
        else
            return "없음"
        return temp
    }

    fun embed(track: AudioTrack, user: User, list: String): MessageEmbed = EmbedBuilder()
            .setTitle("SppuppyBot")
            .setThumbnail(Util.youtubeToThumbnail(track.info.identifier))
            .setDescription("재생중 -> [${track.info.title}](${track.info.uri})\n신청자: `${user.name}`\n")
            .addField("남은 대기열", list, false)
            .build()
}