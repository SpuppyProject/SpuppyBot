package com.github.yeoj34760.spuppybot.music.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

@CommandSettings(name = "disconnect")
object Disconnect : Command() {

    override fun execute(event: CommandEvent) {
        if (event.guild.audioManager.isConnected) {
            event.guild.audioManager.closeAudioConnection()

            event.channel.sendMessage("나왔어요!").queue()
            return
        }


        event.channel.sendMessage("지금 바깥에 나가있어요!").queue()
    }
}