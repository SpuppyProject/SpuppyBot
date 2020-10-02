package com.github.yeoj34760.spuppybot.box


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB

@CommandSettings(name = "removeallbox")
object RemoveAllBox : Command() {
    override fun execute(event: CommandEvent) {
        UserDB(event.author.idLong).boxRemoveAll()
        event.channel.sendMessage("모두 삭제했어요").queue()
    }
}