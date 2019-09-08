package com.github.yeoj34760.spuppybot.Music.Command.Play;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.utility.MergeArray;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Search extends MusicStart {
	EventWaiter waiter;
	public Search(MyGuild myGuild,  EventWaiter waiter) {
		super(myGuild);
		this.waiter = waiter;
	}

	@Override
	public void result(CommandEvent event) {
		
		YoutubeSearchProvider searchProvider = new YoutubeSearchProvider(new YoutubeAudioSourceManager());
		String message = MergeArray.Message(event.getMessage().getContentRaw().split(" ", 100));
		AudioPlaylist track = (AudioPlaylist) searchProvider.loadSearchResult(message);
		String text = "";
		for (int i = 1; i <= 5; i++) {
			text += "**[" + i + "]**" + " " + track.getTracks().get(i - 1).getInfo().title + '\n';
		} //검색 결과를 5번째까지 보여줍니다.
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("번호를 선택하세요", null);
		embed.setAuthor("SpuppyBot", SpuppybotUri.Github,
				SpuppybotUri.Icon_128);
		embed.setColor(Color.blue);
		embed.setDescription(text);
		event.reply(embed.build());
		
		waiter.waitForEvent(MessageReceivedEvent.class, //대화식 생성
				// make sure it's by the same user, and in the same channel, and for safety, a
				// different message
				e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())
						&& !e.getMessage().equals(event.getMessage()), //설명하기 귀찮음
				// respond, inserting the name they listed into the response
				e -> {
					start(event, track.getTracks().get(Integer.parseInt(e.getMessage().getContentRaw()) - 1).getInfo().uri); //음악을 재생합니다.
				},
				// if the user takes more than a minute, time out
				1, TimeUnit.MINUTES, () -> { //1분이상 유저가 대답을 안하다면
					event.reply("시간 초과됨");
					return;
				});
	}
}
