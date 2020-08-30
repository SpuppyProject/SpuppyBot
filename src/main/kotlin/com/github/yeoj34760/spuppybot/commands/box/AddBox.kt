package com.github.yeoj34760.spuppybot.commands.box

import com.github.yeoj34760.spuppybot.Settings
import com.github.yeoj34760.spuppybot.command.Command
import com.github.yeoj34760.spuppybot.command.CommandEvent
import com.github.yeoj34760.spuppybot.command.CommandInfoName
import com.github.yeoj34760.spuppybot.other.Util
import com.github.yeoj34760.spuppybot.playerManager
import com.github.yeoj34760.spuppybot.sql.SpuppyDBController
import com.github.yeoj34760.spuppybot.sql.userbox.UserBoxInfo
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.util.*
/**
 * url이용하여 관련 정보를 얻어서 직렬화로 작업 한 뒤에 base64로 인코딩하여 데이터베이스에 저장합니다.
 * 트랙 정보(UserBoxInfo) -> json(직렬화) -> Base64(인코딩)
 * 데이터베이스에서 트랙 정보얻을 때 반대 반향으로 진행함.
 */
object AddBox : Command(CommandInfoName.ADD_BOX){
    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.channel.sendMessage("명령어를 제대로 적어주세요.\n예시: `${Settings.PREFIX}box add https://youtu.be/HZo539SVHJ4`").queue()
            return
        }
        event.channel.sendMessage("검색 중...").queue {
            if (Util.checkURL(event.argsToString())) {
                playerManager.loadItem(event.argsToString(), object : AudioLoadResultHandler {
                    override fun loadFailed(exception: FriendlyException) {
                        it.editMessage("umm... 불행하게도 로드 실패했어요.").queue()
                    }

                    override fun trackLoaded(track: AudioTrack) {
                        sendBox(event, track)
                        it.editMessage("추가 됨!").queue()
                    }

                    override fun noMatches() {
                        it.editMessage("umm... 유감스럽게도 매치가 읎어요").queue()
                    }

                    override fun playlistLoaded(playlist: AudioPlaylist) {
                        playlist.tracks.forEach { track ->
                            sendBox(event, track)
                        }
                    }
                })
            }
            else {
                TODO("비 url 입력 받을 시 대응 기능 추가")
                it.editMessage("url내놔").queue()
                Util.youtubeSearch(event.argsToString(), it)
            }
        }
    }

    private fun sendBox(event: CommandEvent, track: AudioTrack) {
        val info = UserBoxInfo(
                track.info.title,
                track.info.length,
                track.info.uri,
                event.author.name,
                track.info.author
        )
        val infoBase64 = Base64.getEncoder().encodeToString(Json.encodeToString(info).toByteArray())
        SpuppyDBController.addUserBox(event.author.idLong, infoBase64)
    }
}