package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.FilterCommand
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.db.UserDBController
import com.github.yeoj34760.spuppybot.db.UserMoneyDBController

object FilterCommandImpl : FilterCommand {
    override fun execute(event: CommandEvent): Boolean {
        return if (UserDBController.checkUser(event.author.idLong)) {
            if (!UserMoneyDBController.checkMoneyUser(event.author.idLong))
                UserMoneyDBController.createMoneyUser(event.author.idLong)

            true
        } else {
            event.channel.sendMessage("확인해봤는데 가입 안되어 있네요.\n가입하시려면 `${settings.prefix}가입` 명령어를 이용해주세요.").queue()
            false
        }
    }
}