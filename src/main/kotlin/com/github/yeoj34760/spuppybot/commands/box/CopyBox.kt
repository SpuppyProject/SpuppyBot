package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.music.GuildManager
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack


object CopyBox : Command(CommandInfoName.COPY_BOX) {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty() || event.args[0].toIntOrNull() == null)
            event.channel.sendMessage("명령어를 제대로 써주세요. \n`예시: ${Settings.PREFIX}copy box 1`").queue()

        if (!event.member?.voiceState!!.inVoiceChannel()) {
            event.channel.sendMessage("음성 방에 들어와있어야 합니다. \n").queue()
        }

        playerManager.loadItem(SpuppyDBController.fromUserBox(event.author.idLong)[event.args[0].toInt()+1].info.url, object : AudioLoadResultHandler {
            override fun loadFailed(exception: FriendlyException) {
                event.channel.sendMessage("검색해봤는데 로드가 실패했어요.")
            }

            override fun trackLoaded(track: AudioTrack) {
                Util.autoConnect(event)
                GuildManager.playerControls[event.guildIdLong]!!.playOrAdd(track)
                event.channel.sendMessage("플레이리스트에 `${track.info.title}`을(를) 추가했습니다!").queue()
            }

            override fun noMatches() {
                event.channel.sendMessage("오 이런, 검색이 안 되네요. 해당 영상이 비공개 처리되었는지 확인해보세요.").queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                event.channel.sendMessage("오 이런! 어쨰서인지 플레이리스트 링크로 검색했네요.\n 이 에러가 계속 뜰 경우 개발자한테 문의해 주시 길 바랍니다.").queue()
            }
        })
    }
}