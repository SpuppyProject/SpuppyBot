package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public class NoPlaying implements State {
	@Override
	public EmbedBuilder start(AudioTrack track) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle("음악을 재생합니다.");
		builder.setDescription(track.getInfo().title);
		builder.setThumbnail(video.thumbnail(video.Id(track.getInfo().uri)));
		builder.setFooter(Long.toString(track.getInfo().length));
		return builder;
	}
}
