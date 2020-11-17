package com.github.yeoj34760.spuppybot.music.command.playlist

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.UserDB
import com.github.yeoj34760.spuppybot.db.user.info

@CommandSettings(name="createPlaylist", aliases = ["생성 플레이리스트"])
object CreatePlaylist : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("만들 플레이리스트 이름을 정해주세요!").complete()
            return
        }

        val user = UserDB(event.author.idLong)
        user.createPlaylist(event.content)
        event.channel.sendMessage("만들었어요!").complete()
    }
}