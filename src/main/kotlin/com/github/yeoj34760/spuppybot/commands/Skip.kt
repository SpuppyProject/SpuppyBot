package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.User

object Skip : Command() {
    init {
        super.name = "skip"
        super.aliases = arrayOf("skip", "s", "ㄴ", "나ㅑㅔ")
    }

    override fun execute(event: CommandEvent?) {
        val id = event!!.guild.idLong
        if (GuildManager[id] == null || !GuildManager[id]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생되어 있지 않습니다.").queue()
            return
        }

        val nextTrack = GuildManager[id]!!.skip()

        if (nextTrack != null) {
            event.channel.sendMessage("다음 음악인 `${nextTrack.info.title}`을(를) 재생합니다.").queue()
        }
        else {
            event.channel.sendMessage("다음 음악을 재생하려는데 남은게 없어 현재 음악을 종료해드릴게요").queue()
            GuildManager[id]!!.stop()
            event.guild.audioManager.closeAudioConnection()
        }
    }
}