package com.github.yeoj34760.spuppybot.box


import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.DiscordColor
import com.github.yeoj34760.spuppybot.db.user.info
import net.dv8tion.jda.api.EmbedBuilder

@CommandSettings(name = "listbox")
object ListBox : Command() {
    override fun execute(event: CommandEvent) {
        val listTemp: StringBuffer = StringBuffer()
        var box = event.author.info.box

        if (box.isEmpty()) {
            event.channel.sendMessage("뭔가 엄청난 걸 보여주려고했지만 박스에 아무 것도 없네요.").queue()
            return
        }
        var i = 1
        box.forEach {
            listTemp.append("${i++}. ${it.info.title}\n\n")
        }

        val embed = EmbedBuilder()
                .setColor(DiscordColor.GREEN)
                .setAuthor(event.author.name, null, event.author.avatarUrl)
                .setTitle("`${event.author.name}`의 박스")
                .setDescription(listTemp.toString())
                .build()
        event.channel.sendMessage(embed).queue()
    }
}