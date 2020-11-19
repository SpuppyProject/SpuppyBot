package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.player.CallTrack
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.bot.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Play : Command(name = "play", alias = Bot.commands["play"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        val callTrack = CallTrack.call(event.content)
        if (callTrack.isFailed) {
            event.send(Language.toText("play_error_1", event.author.idLong))
            return
        }

        PlayerUtil.voiceChannelConnect(event)
        val control = PlayerGuildManager.create(event.guild)
        control.play(callTrack.tracks!![0])
    }
}