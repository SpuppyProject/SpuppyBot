package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import com.sedmelluq.discord.lavaplayer.track.AudioTrack

object Skip : Command() {
    init {
        super.name = "skip"
        super.aliases = arrayOf("skip", "s", "ㄴ", "나ㅑㅔ")
    }

    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong
        if (!GuildManager.isTrackCreated(id) || !GuildManager.get(id).isPlayed) {
            event.channel.sendMessage("현재 재생되어 있지 않습니다.").queue()
            return
        }
        val x = GuildManager.get(id).skip()

        event.channel.sendMessage("다음 음악으로 재생합니다. -> `${if (x != null) "${x.audioTrack.info.title}\n신청자: ${x.user.name}"
        else {
            GuildManager.get(id).stop();event.guild.audioManager.closeAudioConnection(); "없음"
        }}`").queue()
    }
}