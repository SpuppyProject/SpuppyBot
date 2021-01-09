package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.player.CallTrack
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.bot.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import java.net.URL

object Play : Command(name = "play", aliases = Bot.commands["play"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        try {
            URL(event.content)
        } catch (e: Exception) {
            search(event); return
        }

        val callTrack = CallTrack.call(event.author, event.content)
        if (callTrack.isFailed) {
            event.send(Language.toText("play_error_1", event.author.idLong))
            return
        }

        PlayerUtil.voiceChannelConnect(event)
        val control = PlayerGuildManager.create(event.guild)


        when (callTrack.playlist) {
            null -> {
                control.play(callTrack.track!!)
                event.send("`${callTrack.track!!.info.title}`을(를) 추가했어요!")
            }
            else -> {
                val playlistName = callTrack.playlist.name
                val playlistCount = callTrack.playlist.tracks.size
                callTrack.playlist.tracks!!.forEach { control.play(it) }
                event.send("`${callTrack.playlist.name}`에 있는 `${playlistCount}`개 음악들을 추가했어요!")
            }
        }
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