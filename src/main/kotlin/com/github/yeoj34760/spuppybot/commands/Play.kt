package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.other.Util.checkURL
import com.github.yeoj34760.spuppybot.other.Util.youtubeSearch
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist


/**
 * 플레이관련 클래스입니다
 */
object Play : Command() {
    init {
        super.name = "play"
        super.aliases = arrayOf("play", "p", "ㅔ")
    }

    override fun execute(event: CommandEvent) {

        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

        //args 값이 없을 경우
        if (event.args.isEmpty()) {
            event.channel.sendMessage("올바르게 써주세요 \n`예시: ?play 진진자라`").queue()
            return
        }

        event.channel.sendMessage("검색 중..").queue {
            if (checkURL(event.args))
                Util.youtubePlay(event, it, event.args)
            else {
                val audioList: AudioPlaylist? = youtubeSearch(event.args, it)
                if (audioList == null)
                    return@queue
                else if (audioList.tracks.isEmpty()) {
                    it.editMessage("검색 결과가 없습니다").queue()
                    return@queue
                }
                Util.youtubePlay(event, it, audioList.tracks[0].info.identifier)
            }
        }
    }
}