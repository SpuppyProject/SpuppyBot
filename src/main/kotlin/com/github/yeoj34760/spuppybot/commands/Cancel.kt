package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController

@CommandSettings(name="Cancel", aliases = ["탈퇴"])
object Cancel : Command() {
    override fun execute(event: CommandEvent) {
        SpuppyDBController.delAllUserBox(event.author.idLong)
        SpuppyDBController.delUser(event.author.idLong)
        event.channel.sendMessage("탈퇴 됨").queue()
    }

}
//object Cancel : Command() {
//    init {
//        name = "cancel"
//        aliases = arrayOf("탈퇴", "취소", "cencel")
//    }
//
//    override fun execute( event: CommandEvent) {
//        if (!SpuppyDBController.checkUser(event.author.idLong)) {
//            event.reply("가입안되어 있네요")
//            return
//        }
//        SpuppyDBController.delAllUserBox(event.author.idLong)
//        SpuppyDBController.delUser(event.author.idLong)
//        event.reply("탈퇴 됨")
//    }
//}