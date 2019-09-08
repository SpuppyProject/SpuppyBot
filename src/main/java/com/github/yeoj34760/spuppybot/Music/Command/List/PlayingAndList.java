package com.github.yeoj34760.spuppybot.Music.Command.List;

import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.github.yeoj34760.spuppybot.utility.Video;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;

public class PlayingAndList implements ListPrint {
	public void start(CommandEvent event, TrackScheduler scheduler) // 재생목록이 있는데 플레이중일 경우
	{
		Video video = new Video();
		AudioTrackInfo AudioInfo = scheduler.getPlayer().getPlayingTrack().getInfo();
		String message = "";
		int i = 1;
		for (AudioTrack track : scheduler.getQueue()) {
			message += "**[" + i++ + "]**" + track.getInfo().title + '\n';
		}
		String thumbnail = video.thumbnail(video.Id(AudioInfo.uri));
		event.reply(new EmbedBuilder().setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128)
				.setTitle("재생중 : " + AudioInfo.title).setThumbnail(thumbnail).appendDescription(message).build());
	}
}
