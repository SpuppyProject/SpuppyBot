package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName

object Disconnect : Command(CommandInfoName.DISCONNECT) {

    override fun execute(event: CommandEvent) {
        if (event.guild.audioManager.isConnected) {
            event.guild.audioManager.closeAudioConnection()

            event.channel.sendMessage("리스트에 있는 음악이 있을 경우 자동으로 초기화합니다.").queue()
            return
        }


        event.channel.sendMessage("이미 나가있네요").queue()
    }
}