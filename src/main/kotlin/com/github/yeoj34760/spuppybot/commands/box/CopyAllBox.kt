package com.github.yeoj34760.spuppybot.commands.box


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserBoxDBController

@CommandSettings(name = "copyallbox")
object CopyAllBox : Command() {
    override fun execute(event: CommandEvent) {
        //유저가 음성 방에 안 들어와 있을 경우
        if (!event.member!!.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와 주세요.").queue()
            return
        }

        Util.autoConnect(event)

        val userBox = UserBoxDBController.fromUserBox(event.author.idLong)

        if (userBox.isEmpty()) {
            event.channel.sendMessage("흠.. 추가하려고 했으나 박스에 아무 것도 없네요.").queue()
            return
        }

        UserBoxDBController.fromUserBox(event.author.idLong).forEach {
            it.audioTrack.userData = event.jda.retrieveUserById(event.author.idLong).complete()
            GuildManager.playerControls[event.guildIdLong]!!.playOrAdd(it.audioTrack)
        }

        event.channel.sendMessage("약 `${userBox.size}`개 음악을 추가했어요!").queue()
    }
}