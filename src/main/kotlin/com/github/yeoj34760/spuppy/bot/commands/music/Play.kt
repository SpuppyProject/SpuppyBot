package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.player.CallTrack
import com.github.yeoj34760.spuppy.bot.player.PlayerControl
import com.github.yeoj34760.spuppy.bot.player.PlayerSendHandler
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.coroutines.coroutineScope

object Play : Command(name = "play", alias = Bot.commands["play"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        val callTrack = CallTrack.call(event.content)
        if (callTrack.isFailed) {
            event.channel.sendMessage(Language.toText("play_error_1", event.author.idLong)).complete()
            return
        }

        val player = Bot.playerManager.createPlayer()
        val playerControl = PlayerControl(player)
        event.guild.audioManager.openAudioConnection(event.member!!.voiceState!!.channel)
        event.guild.audioManager.sendingHandler = PlayerSendHandler(player)
        player.playTrack(callTrack.tracks!![0])
    }
}