package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
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
    val regex = "(\".+?\"|[^ ]+)".toRegex()

    init {
        super.name = "play"
        super.aliases = arrayOf("play", "p", "ㅔ", "플레이")
    }

    override fun execute(event: CommandEvent) = filtering(event)

    private fun filtering(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

        val argsList = regex.findAll(event.args).iterator().withIndex()
        var url: String? = null
        var num: Int? = null
        for ((index, match) in argsList) {
            when (index) {
                0 -> url = match.value
                1 -> num = if (match.value.toIntOrNull() != null &&
                        GuildManager.playerControls[event.guild.idLong] != null &&
                        GuildManager.playerControls[event.guild.idLong]!!.count() >= match.value.toInt() &&
                        match.value.toInt() > 0) match.value.toInt() else null
            }
        }

        //args 값이 없을 경우
        if (event.args.isEmpty() || url == null) {
            event.channel.sendMessage("올바르게 써주세요 \n`예시: ?play 진진자라`").queue()
            return
        }

        search(event, url, num)
    }

    private fun search(event: CommandEvent, url: String, num: Int?) {
        event.channel.sendMessage("검색 중..").queue {
            if (checkURL(url))
                Util.youtubePlay(event, it, url, num)
            else {
                val audioList: AudioPlaylist? = youtubeSearch(url, it)
                if (audioList == null)
                    return@queue
                else if (audioList.tracks.isEmpty()) {
                    it.editMessage("검색 결과가 없습니다").queue()
                    return@queue
                }

                Util.youtubePlay(event, it, audioList.tracks[0].info.identifier, num)
            }
        }
    }

}