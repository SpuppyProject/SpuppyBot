package com.github.yeoj34760.spuppy.bot.commands.music

import com.github.yeoj34760.spuppy.bot.Bot
import com.github.yeoj34760.spuppy.bot.player.PlayerGuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent

object Remove : Command(name = "remove", aliases = Bot.commands["remove"] ?: error("umm..")) {
    override suspend fun execute(event: CommandEvent) {

        if (PlayerGuildManager.check(event.guild.idLong)) {
            event.send("리스트에 아무 것도 없어 remove 명령어를 사용하실 수 없어요!")
            return
        }

        if (event.args.isEmpty() || event.args[0].toIntOrNull() == null) {
            event.send("명령어를 제대로 써주세요!")
            return
        }

        val playerGuildManager = PlayerGuildManager[event.guild]!!
        val inputNumber: Int = event.args[0].toInt() - 1
        val numberOfTrack = playerGuildManager.trackList().count()

        if (numberOfTrack < inputNumber) {
            event.send("입력하신 숫자 값이 playlist 음악 수(${numberOfTrack})보다 높아요!")
            return
        }

        if (inputNumber < 1) {
            event.send("숫자 입력을 잘못 하셨어요!")
            return
        }

        val removedTrack = playerGuildManager.removeTrack(inputNumber)

        event.send("`${removedTrack.info.title}`을(를) 제거했어요.")
    }
}