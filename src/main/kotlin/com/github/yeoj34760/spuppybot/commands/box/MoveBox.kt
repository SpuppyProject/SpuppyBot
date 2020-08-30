package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

object MoveBox : Command(CommandInfoName.MOVE_BOX) {
    override fun execute(event: CommandEvent) {
        if (event.args.size < 2) {
            event.channel.sendMessage("명령어를 올 바르게 해주세요.\n예시: `${Settings.PREFIX}box move 1 2`")
            return
        }
        val order1: Int? = event.args[0].toIntOrNull()
        val order2: Int? = event.args[1].toIntOrNull()
        val max = SpuppyDBController.fromMaxNumber(event.author.idLong)
        if (order1 == null || order2 == null || order1 > max || order1 < 0 || order2 > max || order2 < 0)
        {
            event.channel.sendMessage("올바르게 숫자를 입력해주세요.").queue()
            return
        }
        SpuppyDBController.moveBox(event.guild.idLong, order1, order2)
        event.channel.sendMessage("성공!").queue()
    }
}