package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.other.AudioStartHandler
import com.github.yeoj34760.spuppybot.other.GuildManager
import com.github.yeoj34760.spuppybot.playerManager
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent


/**
 * 플레이관련 클래스입니다
 */
object Play : Command() {
    init {
        super.name = "play"
        super.aliases = arrayOf("play")
    }

    override fun execute(event: CommandEvent) {
        event.member.voiceState!!.channel?.let {
            val id = event.guild.idLong
            val audioManager = event.guild.audioManager
            GuildManager.check(audioManager, id)

            audioManager.openAudioConnection(it)
            playerManager.loadItem(event.args, AudioStartHandler(event, GuildManager.get(id)))
        }
    }
}