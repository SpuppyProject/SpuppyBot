
package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Pause : Command() {
    init {
        super.name = "pause"
        super.aliases = arrayOf("pause")
    }
    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong
        if (!GuildManager.isTrackCreated(id) || !GuildManager.tracks[id]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생되어 있지 않네요").queue()
            return
        }

        var trackScheduler = GuildManager.get(id)
        if (trackScheduler.isPaused()) {
            trackScheduler.resume()
            event.channel.sendMessage("일시정지하였습니다.").queue()
        }
        else {
            trackScheduler.pause()
            event.channel.sendMessage("다시 시작했습니다.").queue()
        }
    }
}