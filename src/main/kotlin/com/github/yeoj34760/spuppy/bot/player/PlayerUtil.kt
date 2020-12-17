package com.github.yeoj34760.spuppy.bot.player

import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioTrack
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist

object PlayerUtil {
    fun voiceChannelConnect(event: CommandEvent): Boolean {
        try {
            if (event.member?.voiceState?.channel == null) return false
            event.guild.audioManager.openAudioConnection(event.member!!.voiceState!!.channel)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun loadPlayerControl(event: CommandEvent): PlayerControl? {
        val control = PlayerGuildManager[event.guild]
        if (control == null || !control.isPlayed()) {
            event.send(Language.toText("not_play", event.author.idLong))
            return null
        }

        return control
    }

    fun youtubeToThumbnail(Identifier: String): String = "https://img.youtube.com/vi/$Identifier/mqdefault.jpg"

    fun youtubeSearch(str: String): AudioPlaylist? {
        return YoutubeSearchProvider().loadSearchResult(str) { YoutubeAudioTrack(it, YoutubeAudioSourceManager()) } as? AudioPlaylist
    }
}