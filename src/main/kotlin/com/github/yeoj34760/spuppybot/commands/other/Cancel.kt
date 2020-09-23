package com.github.yeoj34760.spuppybot.commands.other

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserBoxDBController
import com.github.yeoj34760.spuppybot.sql.spuppydb.UserDBController

@CommandSettings(name="cancel", aliases = ["탈퇴"])
object Cancel : Command() {
    override fun execute(event: CommandEvent) {
        UserBoxDBController.delAllUserBox(event.author.idLong)
        UserDBController.delUser(event.author.idLong)
        event.channel.sendMessage("탈퇴 됨").queue()
    }
}