package com.github.yeoj34760.spuppybot.other

import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.FilterCommand
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.settings

object FilterCommandImpl : FilterCommand {
    override fun execute(event: CommandEvent): Boolean {
        return if (UserDB.idCheck(event.author.idLong))
            true
        else {
            event.channel.sendMessage("확인해보니 가입 안 되어 있어요!\n가입하시려면 `${settings.prefix}가입` 명령어를 이용해주세요!").queue()
            false
        }
    }
}