package com.github.yeoj34760.spuppybot.commands.music

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager

object Remove : Command(CommandInfoName.REMOVE) {

    override fun execute(event: CommandEvent) {
        val playerControl = GuildManager.playerControls[event.guild.idLong]
        if (playerControl == null || playerControl.trackQueue.isEmpty()) {
            event.channel.sendMessage("umm.. 리스트에 음악이 없네요").queue()
            return
        }
        if (event.args.isEmpty() || event.args[0].toIntOrNull() == null) {
            event.channel.sendMessage("숫자를 올바르게 써주세요.").queue()
            return
        }
        val number = event.args[0].toInt()
        playerControl
        when {
            number > playerControl.trackQueue.size -> event.channel.sendMessage("해당 넘버에서 음악이 없네요").queue()
            number < 1 -> event.channel.sendMessage("1 미만을 입력하실 수 없습니다.").queue()
            else -> {
                event.channel.sendMessage("`${playerControl.trackQueue.toTypedArray()[number - 1].info.title}`을(를) 삭제됨").queue()
                playerControl.trackQueue.remove(playerControl.trackQueue.toTypedArray()[number - 1])
            }
        }
    }
}