package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController


@CommandSettings(name = "copybox")
object CopyBox : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty() || event.args[0].toIntOrNull() == null)
            event.channel.sendMessage("명령어를 제대로 써주세요. \n`예시: ${Settings.PREFIX}copy box 1`").queue()

        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와있어야 합니다. \n").queue()
        }

        val userBox = SpuppyDBController.fromUserBox(event.author.idLong)
        if (userBox.isEmpty())
        {
            event.channel.sendMessage("박스에 아무 것도 없네요!").queue()
            return
        }

        if (userBox.size < event.args[0].toInt()-1 || 0 >= event.args[0].toInt())
        {
            event.channel.sendMessage("숫자를 올바르게 써주세요!").queue()
            return
        }
        Util.autoConnect(event)
        
        val track = SpuppyDBController.fromUserBox(event.author.idLong)[event.args[0].toInt() - 1].audioTrack
        track.userData = event.jda.retrieveUserById(event.author.idLong).complete()
        GuildManager.playerControls[event.guildIdLong]!!.playOrAdd(track)
    }
}