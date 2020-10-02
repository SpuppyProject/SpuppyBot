package com.github.yeoj34760.spuppybot.box


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.Util
import net.dv8tion.jda.api.Permission

@CommandSettings(name = "copyallbox")
object CopyAllBox : Command() {
    override fun execute(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member!!.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 있어야 돼요!").queue()
            return
        }
        val channel = event.member!!.voiceState!!.channel!!
        if (!event.guild.selfMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
            event.channel.sendMessage("${event.member.nickname ?: event.author.name}님이 들어와 있는 채널에 권한이 없어 들어갈 수가 없어요!").queue()
            return
        }
        Util.autoConnect(event)

        val box = event.author.info().box

        if (box.isEmpty()) {
            event.channel.sendMessage("흠.. 추가하려고 했으나 박스에 아무 것도 없네요.").queue()
            return
        }

        box.forEach {
            it.userData = event.jda.retrieveUserById(event.author.idLong).complete()
            GuildManager.playerControls[event.guildIdLong]!!.playOrAdd(it)
        }

        event.channel.sendMessage("약 `${box.size}`개 음악을 추가했어요!").queue()
    }
}