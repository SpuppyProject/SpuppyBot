package com.github.yeoj34760.spuppybot.music.command


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import net.dv8tion.jda.api.Permission


@CommandSettings(name = "connect")
object Connect : Command() {
    override fun execute(event: CommandEvent) {
        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어주세요! \n").queue()
        }

        val channel = event.member!!.voiceState!!.channel!!
        if (!event.guild.selfMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
            event.channel.sendMessage("${event.member.nickname ?: event.author.name}님이 들어와 있는 채널에 권한이 없어 들어갈 수가 없어요!").queue()
            return
        }

        event.guild.audioManager.openAudioConnection(channel)
        event.channel.sendMessage("들어왔어요!").queue()
    }
}