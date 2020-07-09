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
            var trackScheduler = GuildManager.get(event.guild.idLong)
            if (trackScheduler!!.isPlayed())
                trackScheduler.pause()

            event.reply("재생중인 음악이 있을 경우 일시적으로 중지합니다. 다시 들어오게 되면 재시작될거예요.")
        }


        event.reply("이미 나가있네요")
    }
}