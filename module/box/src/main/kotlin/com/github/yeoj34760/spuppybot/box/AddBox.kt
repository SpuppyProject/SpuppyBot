package com.github.yeoj34760.spuppybot.box

import com.github.yeoj34760.spuppy.command.Command
import com.github.yeoj34760.spuppy.command.CommandEvent
import com.github.yeoj34760.spuppy.command.CommandSettings
import com.github.yeoj34760.spuppybot.TrackManager.trackToBase64
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.settings
import com.github.yeoj34760.spuppybot.db.UserBoxDBController
import com.github.yeoj34760.spuppybot.music.Util
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import net.dv8tion.jda.api.entities.Message

/**
 * url이용하여 관련 정보를 얻어서 직렬화로 작업 한 뒤에 base64로 인코딩하여 데이터베이스에 저장합니다.
 * 트랙 정보(UserBoxInfo) -> json(직렬화) -> Base64(인코딩)
 * 데이터베이스에서 트랙 정보얻을 때 반대 반향으로 진행함.
 */

@CommandSettings(name = "addbox")
object AddBox : Command() {
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("명령어를 제대로 적어주세요.\n예시: `${settings.prefix}box add https://youtu.be/HZo539SVHJ4`").queue()
            return
        }

        val userBox = UserBoxDBController.fromUserBox(event.author.idLong)
        if (userBox.size >= 10) {
            event.channel.sendMessage("흠.. 추가하려고 했는데 갯수 제한이 걸렸네요\n추가하고 싶다면 박스에 있는 음악들중에 하나 제거해주세요.")
        }

        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(event.content)) {
                searchToUserBox(event, it)
            } else {
                val search = Util.youtubeSearch(event.content, it) ?: return@queue
                if (search.tracks!!.isEmpty()) {
                    event.channel.sendMessage("이런 검색결과가 없네요").queue()
                }
                it.editMessage("`${search.tracks[0].info.title}`를(을) 추가 되었어요!").queue()
                sendBox(event, search.tracks[0])
            }
        }
    }


    private fun searchToUserBox(event: CommandEvent, message: Message) {
        playerManager.loadItem(event.content, object : AudioLoadResultHandler {
            override fun loadFailed(exception: FriendlyException) {
                message.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
            }

            override fun trackLoaded(track: AudioTrack) {
                sendBox(event, track)
                message.editMessage("`${track.info.title}`를(을) 추가했어요!").queue()
            }

            override fun noMatches() {
                message.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                playlist.tracks.forEach { track ->
                    sendBox(event, track)
                }
            }
        })
    }

    private fun sendBox(event: CommandEvent, track: AudioTrack) {
        UserBoxDBController.addUserBox(event.author.idLong, trackToBase64(track))
    }
}