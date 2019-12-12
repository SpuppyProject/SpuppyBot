package com.github.yeoj34760.spuppybot.Music.Command.List;

import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.github.yeoj34760.spuppybot.utility.Video;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;

public class Playing implements ListPrint {
	@Override
	public void start(CommandEvent event, TrackScheduler scheduler) //재생목록이 없는데 재생중일 경우
	{
		Video video = new Video();
		AudioTrackInfo AudioInfo = scheduler.getPlayer().getPlayingTrack().getInfo();
		String thumbnail = video.thumbnail(video.Id(AudioInfo.uri));
		event.reply(new SpuppybotComment("재생중 : " + AudioInfo.title, null, "재생 목록이 없습니다.", SpuppybotColor.blue, thumbnail).get().build());
	}
}
