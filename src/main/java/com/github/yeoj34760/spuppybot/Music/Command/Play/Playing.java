package com.github.yeoj34760.spuppybot.Music.Command.Play;

import java.util.concurrent.TimeUnit;

import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public class Playing implements State { // 음악이 재생중일경우 (재생목록이 있을 경우)
	@Override
	public EmbedBuilder start(AudioTrack track) {
		String time = String.format("%d : %d", 
			    TimeUnit.MILLISECONDS.toMinutes(track.getInfo().length),
			    TimeUnit.MILLISECONDS.toSeconds(track.getInfo().length) - 
			    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.getInfo().length))
			);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle("추가 됨!");
		builder.addField("음악 이름", track.getInfo().title, false);
		builder.addField("만든이", track.getInfo().author, true);
		builder.addField("영상 길이", time, true);
		
		builder.setThumbnail(video.thumbnail(video.Id(track.getInfo().uri)));
		builder.setColor(SpuppybotColor.green);
		return builder;
	}
}
