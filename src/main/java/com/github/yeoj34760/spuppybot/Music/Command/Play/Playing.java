package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public class Playing implements State { // 음악이 재생중일경우 (재생목록이 있을 경우)
	@Override
	public EmbedBuilder start(AudioTrack track) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle("추가 됨!");
		
		builder.setThumbnail(video.thumbnail(video.Id(track.getInfo().uri)));
		builder.setFooter(Long.toString(track.getInfo().length));
		builder.setColor(SpuppybotColor.green);
		return builder;
	}
}
