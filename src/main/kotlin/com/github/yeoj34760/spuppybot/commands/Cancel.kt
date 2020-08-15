package com.github.yeoj34760.spuppybot.commands

import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent

object Cancel : Command() {
    init {
        name = "cancel"
        aliases = arrayOf("탈퇴", "취소" , "cencel")
    }
    override fun execute(event: CommandEvent) {
     if (!SpuppyDBController.checkUser(event.author.idLong))
     {
         event.reply("가입안되어 있네요")
         return
     }
        SpuppyDBController.delAllUserBox(event.author.idLong)
        SpuppyDBController.delUser(event.author.idLong)
        event.reply("탈퇴 됨")
    }
}