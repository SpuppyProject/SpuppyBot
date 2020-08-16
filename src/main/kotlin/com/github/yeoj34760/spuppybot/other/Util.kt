package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppybot.music.AudioStartHandler
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import com.github.yeoj34760.spuppybot.playerManager
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import net.dv8tion.jda.api.entities.Message
import java.text.SimpleDateFormat
import java.util.regex.Pattern

object Util {
    /**
     * 식별자 이용해 썸네일 링크를 반환합니다.
     */
    fun youtubeToThumbnail(Identifier: String): String = "https://img.youtube.com/vi/$Identifier/mqdefault.jpg"


    fun timeMaker(milliseconds: Int) = SimpleDateFormat("HH시 mm분 ss초").format(milliseconds)

    /**
     * 유튜브통해 음악을 재생합니다.
     */
    fun youtubePlay(event: CommandEvent, message: Message, url: String) {
        event.member.voiceState!!.channel?.let {
            val id = event.guild.idLong
            val audioManager = event.guild.audioManager
            GuildManager.check(audioManager, id)
            if (!audioManager.isConnected)
                audioManager.openAudioConnection(it)
            playerManager.loadItem(url, AudioStartHandler(event, message, playerControls[id]!!))
        }
    }

    /**
     * URL인지 검사합니다. true일 경우 url
     */
    fun checkURL(url: String): Boolean {
        val urlRegex = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$"
        return Pattern.compile(urlRegex).matcher(url).find()
    }

    /**
     * 유튜브 검색을 해서 음악 리스트를 불러와 반환합니다.
     */
    //fun youtubeSearch(query: String): AudioPlaylist? = YoutubeSearchProvider().loadSearchResult(query) { YoutubeAudioTrack(it, YoutubeAudioSourceManager()) } as? AudioPlaylist
    fun youtubeSearch(query: String, errorMessage: Message): AudioPlaylist? {
        return try {
            YoutubeSearchProvider().loadSearchResult(query) { YoutubeAudioTrack(it, YoutubeAudioSourceManager()) } as? AudioPlaylist

        } catch (e: Exception) {
            errorMessage.editMessage("오류가 발생했습니다. \nlog : ${e.message}").queue()
            null
        }
    }
}