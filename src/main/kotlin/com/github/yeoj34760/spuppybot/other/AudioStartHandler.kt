package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppybot.other.Util.youtubeToThumbnail
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AudioStartHandler(val event: CommandEvent,val message: Message , val trackScheduler: TrackScheduler) : AudioLoadResultHandler {
    //로드 실패시
    override fun loadFailed(exception: FriendlyException) {
        message.editMessage("umm.. 로드 시도해 보았는데 영 좋지 않네요").queue()
    }

    override fun trackLoaded(track: AudioTrack) {
        /*val embed: MessageEmbed = EmbedBuilder()
                .setTitle("SpuppyBot")
                .addField("만든이", track.info.author, false)
                .addField("음성 채널", event.member.voiceState!!.channel!!.name, true)
                .addField("길이", sdf.format(track.info.length), true)
                .addField("URL", "[클릭](${track.info.uri})", false)
                .addField("대기열", if (trackScheduler.count() == 0 && !trackScheduler.isPlayed) "없음" else  "${trackScheduler.count()+1}남음", true)
                .setThumbnail(youtubeToThumbnail(track.info.identifier))
                .build()
        message.editMessage(embed).content("성공!").queue()*/
        message.editMessage("플레이리스트에 추가했습니다. -> `${track.info.title}`").queue()
        track.userData = event.author
        trackScheduler.playOrAdd(track)
    }

    override fun noMatches() {
        message.editMessage("Error: 음악을 찾지 못했습니다.").queue()
    }

    override fun playlistLoaded(playlist: AudioPlaylist) {
       /* playlist.tracks.forEach {
            it.userData = event.author
            trackScheduler.playOrAdd(it)
        }*/
        try {
            for (i in 0 until playlist.tracks.size)
            {
                playlist.tracks[i].userData = event.author
                trackScheduler.playOrAdd(playlist.tracks[i])
            }
            message.editMessage("`${playlist.name}`의 `${playlist.tracks.size}`개 음악을 플레이리스트에 추가했습니다.").queue()
        } catch (e: Exception) {
            println(e)
        }
    }

}