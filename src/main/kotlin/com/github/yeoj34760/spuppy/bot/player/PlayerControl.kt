package com.github.yeoj34760.spuppy.bot.player

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter

class PlayerControl(private val player: AudioPlayer) : AudioEventAdapter() {
}