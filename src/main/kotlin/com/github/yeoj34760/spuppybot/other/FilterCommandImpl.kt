package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.FilterCommand
import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

object FilterCommandImpl : FilterCommand {
    override fun execute(event: CommandEvent): Boolean {
        return if (SpuppyDBController.checkUser(event.author.idLong)) {
            if (!SpuppyDBController.checkMoneyUser(event.author.idLong))
                SpuppyDBController.addMoneyUser(event.author.idLong)
            true
        }
        else {
            event.channel.sendMessage("확인해봤는데 가입 안되어 있네요.\n가입하시려면 `${settings.prefix}가입` 명령어를 이용해주세요.").queue()
            false
        }
    }
}