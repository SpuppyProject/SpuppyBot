package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

/**
 * 볼륨 기능을 제공하기 위한 오브젝트입니다.
 */
object Volume : Command(CommandInfoName.VOLUME) {

    override fun execute(event: CommandEvent) {
        if (!SpuppyDBController.checkGuild(event.guild.idLong))
            SpuppyDBController.addGuild(event.guild.idLong)

        val id = event.guild.idLong
        val audioManager = event.guild.audioManager
        GuildManager.check(audioManager, id)

        if (event.args[0].toIntOrNull() == null) {
            event.channel.sendMessage("숫자를 제대로 써주세요.").queue()
        }

        val argsInt: Int = event.args[0].toInt()

        if (argsInt in 0..100) {
            playerControls[id]?.volume(argsInt)
            SpuppyDBController.guildVolume(id, argsInt)
            event.channel.sendMessage("`$argsInt`(으)로 볼륨 조정했습니다.").queue()
        } else
            event.channel.sendMessage("죄송합니다만, 숫자를 1 ~ 100으로 지정해주세요.").queue()
    }
}