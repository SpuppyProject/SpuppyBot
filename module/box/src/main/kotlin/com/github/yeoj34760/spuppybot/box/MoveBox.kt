package com.github.yeoj34760.spuppybot.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info


@CommandSettings(name = "movebox")
object MoveBox : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.size < 2) {
            event.channel.sendMessage("명령어를 올 바르게 해주세요.\n예시: `${settings.prefix}box move 1 2`")
            return
        }
        val order1: Int? = event.args[0].toIntOrNull()
        val order2: Int? = event.args[1].toIntOrNull()
        val max = event.author.info.box.size
        if (max <= 1) {
            event.channel.sendMessage("음악 옮기려는데 하나밖에 없네요.").queue()
            return
        }
        if (order1 == null || order2 == null || order1 > max || order1 < 0 || order2 > max || order2 < 0) {
            event.channel.sendMessage("올바르게 숫자를 입력해주세요.").queue()
            return
        }

        UserDB(event.author.idLong).boxMove(order1-1, order2-1)
        event.channel.sendMessage("성공!").queue()
    }
}