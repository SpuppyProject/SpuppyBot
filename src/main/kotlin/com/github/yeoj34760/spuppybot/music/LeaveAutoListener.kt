package com.github.yeoj34760.spuppybot.music

import com.github.yeoj34760.spuppybot.music.GuildManager.playerControls
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.VoiceChannel
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration


/**
 * 봇이 나갔을 때 자동으로 음악을 중지하도록 합니다, 또한 봇이 들어와 있는 음성 채널에 아무도 없으면 자동으로 나가지도록 합니다.
 */

object LeaveAutoListener : ListenerAdapter() {
  private  val timers = mutableMapOf<Long, Job>()
    @ExperimentalTime
    override fun onGuildVoiceLeave(event: GuildVoiceLeaveEvent) {
        if (event.member.idLong == event.jda.selfUser.idLong) {
            val playerControl = playerControls[event.guild.idLong] ?: return
            stopMusic(playerControl)
            val timer = timers[event.guild.idLong] ?: return
            timer.cancel()
            timers.remove(event.guild.idLong)
            return
        }

        val connectedChannel = event.guild.audioManager.connectedChannel?.idLong ?: return

        if (connectedChannel == event.channelLeft.idLong && event.channelLeft.members.size <= 1) {
            timerStart(event.guild)
        }
    }

    @ExperimentalTime
    override fun onGuildVoiceMove(event: GuildVoiceMoveEvent) {
        val connectedChannel = event.guild.audioManager.connectedChannel ?: return
        val timer = timers[event.guild.idLong]
        println(event.channelJoined.members)
       if(event.channelJoined.idLong == connectedChannel.idLong && timer != null && !timer.isCancelled) {
           timer.cancel()
           timers.remove(event.guild.idLong)
           return
        }
        else if(connectedChannel.idLong != event.channelJoined.idLong &&  event.channelLeft.members.size <= 1) {
            timerStart(event.guild)
        }
    }
    @ExperimentalTime
    private fun timerStart(guild:Guild) {
        val job = GlobalScope.launch {
            delay(1.toDuration(DurationUnit.MINUTES))
            guild.audioManager.closeAudioConnection()
            val playerControl = playerControls[guild.idLong] ?: return@launch
            stopMusic(playerControl)
        }

        timers[guild.idLong] = job
    }
    override fun onGuildVoiceJoin(event: GuildVoiceJoinEvent) {
        val connectedChannel = event.guild.audioManager.connectedChannel?.idLong ?: return
        val timer = timers[event.guild.idLong] ?: return
        
        if (event.channelJoined.idLong == connectedChannel && !timer.isCancelled) {
            timer.cancel()
            timers.remove(event.guild.idLong)
        }
    }
    private fun stopMusic(playerControl: PlayerControl?) {
        if (playerControl != null && playerControl.isPlayed()){
            playerControl.stop()
            playerControl.isLooped = false
            playerControl.resume()
        }
    }
}