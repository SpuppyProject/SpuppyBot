package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.utility.Video;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public interface State {
	public Video video = new Video();

	public EmbedBuilder start(AudioTrack track); //멘트정합니다.
}
