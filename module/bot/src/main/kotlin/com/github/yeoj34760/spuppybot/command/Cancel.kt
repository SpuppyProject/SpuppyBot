package com.github.yeoj34760.spuppybot.command

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB

@CommandSettings(name = "cancel", aliases = ["탈퇴"])
object Cancel : Command() {
    override fun execute(event: CommandEvent) {
        val userDB = UserDB(event.author.idLong)
        if (!userDB.check()) {
            event.channel.sendMessage("가입되어 있지 않아요").queue()
            return
        }

        userDB.remove()
        event.channel.sendMessage("탈퇴되었어요. 다시 돌아오길 바랄게요.").queue()
    }
}