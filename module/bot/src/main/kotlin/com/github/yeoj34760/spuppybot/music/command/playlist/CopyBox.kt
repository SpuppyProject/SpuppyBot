package com.github.yeoj34760.spuppybot.music.command.playlist

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.db.user.info
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.music.Util
import net.dv8tion.jda.api.Permission


@CommandSettings(name = "copybox")
object CopyBox : Command() {
    override fun execute(event: CommandEvent) {
//        if (event.args.isEmpty() || event.args[0].toIntOrNull() == null)
//            event.channel.sendMessage("명령어를 제대로 써주세요. \n`예시: ${settings.prefix}copy box 1`").queue()
//
//        if (!event.member?.voiceState!!.inVoiceChannel()) {
//            event.channel.sendMessage("음성 방에 들어와있어야 해요 \n").queue()
//        }
//
//        val channel = event.member!!.voiceState!!.channel!!
//        if (!event.guild.selfMember.hasPermission(channel, Permission.VOICE_CONNECT)) {
//            event.channel.sendMessage("${event.member.nickname ?: event.author.name}님이 들어와 있는 채널에 권한이 없어 들어갈 수가 없어요!").queue()
//            return
//        }
//
//        val box = event.author.info().box
//
//        if (box.isEmpty()) {
//            event.channel.sendMessage("박스에 아무 것도 없어요!").queue()
//            return
//        }
//
//        if (box.size < event.args[0].toInt() - 1 || 0 >= event.args[0].toInt()) {
//            event.channel.sendMessage("숫자를 올바르게 써주세요!").queue()
//            return
//        }
//        Util.autoConnect(event)
//
//        val track = box[event.args[0].toInt() - 1]
//        track.userData = event.jda.retrieveUserById(event.author.idLong).complete()
//        GuildManager.playerControls[event.guildIdLong]!!.playOrAdd(track)
    }
}