package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.music.GuildManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Disconnect : Command() {
    init {
        name = "disconnect"
        aliases = arrayOf("disconnect", "꺼져", "나가", "얀", "dis")
    }
    override fun execute(event: CommandEvent) {
        if (event.guild.audioManager.isConnected) {
            event.guild.audioManager.closeAudioConnection()

            event.reply("리스트에 있는 음악이 있을 경우 자동으로 초기화합니다.")
            return
        }


        event.reply("이미 나가있네요")
    }
}