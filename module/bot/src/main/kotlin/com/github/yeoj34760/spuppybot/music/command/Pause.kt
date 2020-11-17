package com.github.yeoj34760.spuppybot.music.command

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings


@CommandSettings(name = "pause")
object Pause : Command() {

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong
        if (GuildManager.playerControls[id] == null || !GuildManager.playerControls[id]!!.isPlayed()) {
            event.channel.sendMessage("무엇가 재생되어 있지 않아요..").queue()
            return
        }
        val trackScheduler = GuildManager.playerControls[id]!!
        if (trackScheduler.isPaused()) {
            trackScheduler.resume()
            event.channel.sendMessage("다시 시작했어요!").queue()
        } else {
            trackScheduler.pause()
            event.channel.sendMessage("일시정지 했어요!").queue()
        }
    }
}