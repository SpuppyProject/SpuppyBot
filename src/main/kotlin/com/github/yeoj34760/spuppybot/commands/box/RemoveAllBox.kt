package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController


object RemoveAllBox : Command(CommandInfoName.REMOVE_ALL_BOX) {
    override fun execute(event: CommandEvent) {
        SpuppyDBController.delAllUserBox(event.author.idLong)
        event.channel.sendMessage("모두 삭제했습니다.").queue()
    }
}