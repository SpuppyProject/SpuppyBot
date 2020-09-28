package com.github.yeoj34760.spuppybot.commands.other

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserBoxDBController
import com.github.yeoj34760.spuppybot.db.UserDBController
import com.github.yeoj34760.spuppybot.db.UserMoneyDBController

@CommandSettings(name = "cancel", aliases = ["탈퇴"])
object Cancel : Command() {
    override fun execute(event: CommandEvent) {
        if (!UserDBController.checkUser(event.author.idLong)) {
            event.channel.sendMessage("가입되어 있지 않습니다!").queue()
            return
        }
        UserBoxDBController.delAllUserBox(event.author.idLong)
        UserMoneyDBController.delMoneyUser(event.author.idLong)
        UserDBController.delUser(event.author.idLong)
        event.channel.sendMessage("탈퇴되었어요. 다시 돌아오길 바랄게요.").queue()
    }
}