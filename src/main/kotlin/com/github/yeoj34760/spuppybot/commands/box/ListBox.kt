package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import net.dv8tion.jda.api.EmbedBuilder

object ListBox : Command(CommandInfoName.LIST) {
    override fun execute(event: CommandEvent) {
        val listTemp: StringBuffer = StringBuffer()
        var userBoxs = SpuppyDBController.fromUserBox(event.author.idLong)

        if (userBoxs.isEmpty()) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여주려고했지만 박스에 아무 것도 없네요.").queue()
        }
        for (x in 1..userBoxs.size) {
            val userBox = userBoxs.stream().filter { it.order == x }.findAny().get()
            listTemp.append("${userBox.order}. ${userBox.info.title}\n\n")
        }

        val embed = EmbedBuilder()
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setTitle("`${event.author.name}`의 박스")
                .setDescription(listTemp.toString())
                .build()
        event.channel.sendMessage(embed).queue()
    }
}