package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.FilterCommand
import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

object FilterCommandImpl : FilterCommand {
    override fun execute(event: CommandEvent): Boolean {

        //예외 명령어
        if (event.message.contentRaw == "${Settings.PREFIX}가입" ||
                event.message.contentRaw == "${Settings.PREFIX}동의")
            return true

        return if (SpuppyDBController.checkUser(event.author.idLong))
            true
        else {
            event.channel.sendMessage("확인해봤는데 가입 안되어 있네요.\n가입하시려면 `${Settings.PREFIX}가입` 명령어를 이용해주세요.").queue()
            false
        }
    }
}