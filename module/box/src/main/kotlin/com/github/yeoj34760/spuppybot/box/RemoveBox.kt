package com.github.yeoj34760.spuppybot.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info

@CommandSettings(name = "removebox")
object RemoveBox : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("명령어를 올바르게 써주세요.\n예시: `${settings.prefix}box remove 1`").queue()
            return
        }
        val max = event.author.info().box.size

        when (event.args[0].toIntOrNull()) {
            !in 1..max -> {
                event.channel.sendMessage("1 ~ $max 입력해주세요.").queue(); return
            }
            null -> {
                event.channel.sendMessage("숫자 올바르게 써주세요.").queue(); return
            }
        }

        UserDB(event.author.idLong).boxRemove(event.args[0].toInt()-1)
        event.channel.sendMessage("삭제되었어요!").queue()
    }
}