package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.utilities.language.Language
import com.github.yeoj34760.spuppy.utilities.player.CallTrack
import com.github.yeoj34760.spuppy.utilities.player.PlayerControl
import com.github.yeoj34760.spuppy.utilities.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.utilities.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import java.net.URL

/**
 * if bot have searched but doesn't have playlist to will call onlyTrackPlay.
 */
object Play : Command(name = "play", aliases = Bot.commands["play"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        try {
            URL(event.content)
        } catch (e: Exception) {
            search(event); return
        }

        url(event)
    }

    private suspend fun url(event: CommandEvent) {
        val callTrack = CallTrack.call(event.author, event.content)
        if (callTrack.isFailed) {
            event.send(Language.toText("play_error_1", event.author.idLong))
            return
        }

        PlayerUtil.voiceChannelConnect(event)
        val control = PlayerGuildManager.create(event.guild)

        when (callTrack.playlist) {
            null -> onlyTrackPlay(event, control, callTrack.track!!)
            else -> manyTrackPlay(event, control, callTrack.playlist)
        }
    }

    private fun onlyTrackPlay(event: CommandEvent,control: PlayerControl, track: AudioTrack) {
        control.play(track)
        event.send("`${track.info.title}`을(를) 추가했어요!")
    }

    private fun manyTrackPlay(event: CommandEvent, control: PlayerControl, audioPlaylist: AudioPlaylist) {
        val playlistName = audioPlaylist.name
        val playlistCount = audioPlaylist.tracks.size
        audioPlaylist.tracks!!.forEach { control.play(it) }
        event.send("`${audioPlaylist.name}`에 있는 `${playlistCount}`개 음악들을 추가했어요!")
    }

    private fun search(event: CommandEvent) {
        val list = PlayerUtil.youtubeSearch(event.content)
        if (list != null && list?.tracks?.isNotEmpty()!!) {
            val track = list.tracks[0]
            event.send("`${track.info.title}`을(를) 추가했어요!")
            PlayerUtil.voiceChannelConnect(event)
            PlayerGuildManager.create(event.guild).play(track)
            return
        }

        event.send("검색을 해봤지만 거기엔 아무 것도 없었어요!")
    }
}