package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.other.Util.checkURL
import com.github.yeoj34760.spuppybot.other.Util.youtubeSearch
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist

/**
 * 플레이관련 클래스입니다
 */
object Play : Command(CommandInfoName.PLAY) {
    val regex = "(\".+?\"|[^ ]+)".toRegex()

    override fun execute(event: CommandEvent) = filtering(event)

    private fun filtering(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

//        var url: String? = null
//        var num: Int? = null
//        for ((index, value) in event.args) {
//            when (index) {
//                0 -> url = match.value
//                1 -> num = if (match.value.toIntOrNull() != null &&
//                        GuildManager.playerControls[event.guild.idLong] != null &&
//                        GuildManager.playerControls[event.guild.idLong]!!.count() >= match.value.toInt() &&
//                        match.value.toInt() > 0) match.value.toInt() else null
//            }
//        }

        //args 값이 없을 경우
        if (event.args.isEmpty()) {
            event.channel.sendMessage("올바르게 써주세요 \n`예시: ?play 진진자라`").queue()
            return
        }

        search(event)
    }

    private fun search(event: CommandEvent) {
        println(event.argsToString())
        event.channel.sendMessage("검색 중..").queue {
            if (checkURL(event.argsToString()))
                Util.youtubePlay(event, it, event.argsToString())
            else {
                val audioList: AudioPlaylist? = youtubeSearch(event.argsToString(), it)
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