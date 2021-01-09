package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.language.Language
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.bot.player.PlayerUtil
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Skip : Command(name="skip", aliases =  Bot.commands["skip"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {
        val control = PlayerUtil.loadPlayerControl(event) ?: return
        val firstTrack = control.trackList().firstOrNull()
        control.skip()

        when (firstTrack) {
            null -> event.send("현재 재생중인 음악을 중지했어요!")
            else -> event.send("다음 음악인 `${firstTrack.info.title}`을(를) 재생을 할게요!")
        }
    }
}