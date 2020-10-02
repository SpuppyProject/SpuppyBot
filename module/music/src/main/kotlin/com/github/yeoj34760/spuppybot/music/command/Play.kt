package com.github.yeoj34760.spuppybot.music.command

import com.github.yeoj34760.spuppybot.music.Util
import com.github.yeoj34760.spuppybot.music.Util.checkURL
import com.github.yeoj34760.spuppybot.music.Util.youtubeSearch
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import net.dv8tion.jda.api.Permission

/**
 * 플레이관련 클래스입니다
 */

@CommandSettings(name = "play")
object Play : Command() {

    override fun execute(event: CommandEvent) {
        filtering(event)
    }

    private fun filtering(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요!").queue()
            return
        }

        val channel = event.member!!.voiceState!!.channel!!
        if (!event.guild.selfMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
            event.channel.sendMessage("${event.member.nickname ?: event.author.name}님이 들어와 있는 채널에 권한이 없어 들어갈 수가 없어요!").queue()
            return
        }

        //args 값이 없을 경우
        if (event.args.isEmpty()) {
            event.channel.sendMessage("올바르게 써주세요 \n`예시: ?play 진진자라`").queue()
            return
        }

        search(event)
    }

    private fun search(event: CommandEvent) {
        event.channel.sendMessage("엄청난 걸 보여드리기 위해 검색 중..").queue {
            if (checkURL(event.content))
                Util.youtubePlay(event, it, event.content)
            else {
                val audioList: AudioPlaylist? = youtubeSearch(event.content, it)
                if (audioList == null)
                    return@queue
                else if (audioList.tracks.isEmpty()) {
                    it.editMessage("검색 결과가 없어요").queue()
                    return@queue
                }

                Util.youtubePlay(event, it, audioList.tracks[0].info.identifier)
            }
        }
    }

}