package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.settings

import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserBoxDBController

@CommandSettings(name = "removebox")
object RemoveBox : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("명령어를 제대로 써주세요.\n예시: `${settings.prefix}box remove 1`").queue()
            return
        }
        val max = UserBoxDBController.fromMaxNumber(event.author.idLong)

        when (event.args[0].toIntOrNull()) {
            !in 1..max -> {
                event.channel.sendMessage("1 ~ $max 입력해주세요.").queue(); return
            }
            null -> {
                event.channel.sendMessage("숫자 올바르게 써주세요.").queue(); return
            }
        }

        UserBoxDBController.delUserBox(event.author.idLong, event.args[0].toInt())
        event.channel.sendMessage("삭제완료").queue()
    }
}