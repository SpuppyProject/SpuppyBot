package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

@CommandSettings(name = "skip")
object Skip : Command() {

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong
        if (playerControls[id] == null || !playerControls[id]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생되어 있지 않습니다.").queue()
            return
        }

        when (val nextTrack = playerControls[id]!!.skip()) {
            null -> {
                event.channel.sendMessage("흠.. 다음 음악이 없네요!").queue()
                playerControls[id]!!.stop()
                event.guild.audioManager.closeAudioConnection()
            }
            else -> {
                event.channel.sendMessage("다음 음악인 `${nextTrack.info.title}`을(를) 재생합니다.").queue()
            }
        }
    }
}