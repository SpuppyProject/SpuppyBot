package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioTrack

object Skip : Command() {
    init {
        super.name = "skip"
        super.aliases = arrayOf("skip")
    }
    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong
        if (!GuildManager.isTrackCreated(id) || !GuildManager.tracks[id]!!.isPlayed) {
            event.channel.sendMessage("현재 재생되어 있지 않습니다.").queue()
            return
        }
        var x: AudioTrack? = GuildManager.get(id)!!.skip()
        if (x != null)
           event.channel.sendMessage("다음 음악으로 재생합니다. -> `${x.info.title}`").queue()
        else
            event.channel.sendMessage("다음 음악으로 재생하려 하지만 더 이상 남은게 없네요").queue()
    }
}