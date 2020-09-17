package com.github.yeoj34760.spuppybot.commands.music


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls


@CommandSettings(name = "skip")
object Skip : Command() {

    override fun execute(event: CommandEvent) {
        val id = event.guild.idLong
        if (playerControls[id] == null || !playerControls[id]!!.isPlayed()) {
            event.channel.sendMessage("현재 재생되어 있지 않습니다.").queue()
            return
        }

        val nextTrack = playerControls[id]!!.skip()

        if (nextTrack != null) {
            event.channel.sendMessage("다음 음악인 `${nextTrack.info.title}`을(를) 재생합니다.").queue()
        } else {
            event.channel.sendMessage("다음 음악을 재생하려는데 남은게 없어 현재 음악을 종료해드릴게요").queue()
            playerControls[id]!!.stop()
            event.guild.audioManager.closeAudioConnection()
        }
    }
}