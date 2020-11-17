package com.github.yeoj34760.spuppybot.music.command.playlist

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings

@CommandSettings(name = "box")
object Box : Command() {
    override fun execute(event: CommandEvent) {
        event.channel.sendMessage("박스 명령어를 알고 싶다면 `http://spuppy.ml/spuppybot/help`에 확인해보세요!").queue()
    }
}