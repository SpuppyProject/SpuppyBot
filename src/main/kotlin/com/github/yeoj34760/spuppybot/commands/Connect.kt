package com.github.yeoj34760.spuppybot.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dv8tion.jda.api.entities.VoiceChannel

object Connect : Command() {
    init {
        super.name = "connect"
        super.aliases = arrayOf("connect", "연결", "c", "ㅊ")
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty() && !event.member.voiceState!!.inVoiceChannel()) {
            event.reply("음성 방에 들어오시거나 방 이름을 적어주세요. \n")
        }
        if (event.args.isNotEmpty()) {
            event.guild.voiceChannels.forEach {
                if (event.args == it.name) {
                    event.reply("`${it.name}`에 들어왔습니다.")
                    event.guild.audioManager.openAudioConnection(it)
                    return
                }
            }
            event.reply("해당 이름을 가진 음성 채널을 못 찾았습니다.")
        }
    }
}