package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppybot.other.Util.youtubeToThumbnail
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.text.SimpleDateFormat
import java.util.*

class AudioStartHandler(val event: CommandEvent, val trackScheduler: TrackScheduler) : AudioLoadResultHandler {
    //로드 실패시
    override fun loadFailed(exception: FriendlyException) {

    }

    override fun trackLoaded(track: AudioTrack) {
        var sdf = SimpleDateFormat("HH시 mm분 ss초")

        var embed: MessageEmbed = EmbedBuilder()
                .setTitle("SpuppyBot")
                .addField("만든이", track.info.author, false)
                .addField("음성 채널", event.member.voiceState!!.channel!!.name, true)
                .addField("길이", sdf.format(track.info.length), true)
                .addField("URL", "[Clink](${track.info.uri})", false)
                .addField("대기열", "${if (trackScheduler.count() == 0 && !trackScheduler.isPlayed) "없음" else  "${trackScheduler.count()+1}남음"}", true)
                .setThumbnail(youtubeToThumbnail(track.info.identifier))
                .build()
        event.channel.sendMessage(embed).queue()
        trackScheduler.playOrAdd(track)
    }

    override fun noMatches() {
        event.channel.sendMessage("Error: 음악을 찾지 못했습니다.").queue()
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
        event.channel.sendMessage("플레이리스트를 왜 불러옴").queue()
    }

}