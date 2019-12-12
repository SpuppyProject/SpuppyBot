package com.github.yeoj34760.spuppybot.Music.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;

public class RemoveCommand extends Command {
final MyGuild myGuild;
	public RemoveCommand(MyGuild myGuild) {
		this.myGuild = myGuild;
		this.name = "삭제";
		this.aliases = new String[] { "삭제", "remove", "파괴", "제거" };
		this.help = "대기열중에 특정 음악을 제외시킵니다.";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		List<AudioTrack> temp = new ArrayList<AudioTrack>();
		TrackScheduler scheduler = myGuild.getGuildAudioPlayer(event.getGuild()).scheduler;
		int i = Integer.parseInt(event.getArgs()) - 1;
		int l = 0;
		for (AudioTrack track : scheduler.getQueue()) {
			if (i != l)
				temp.add(track);
			l++;
			}
		scheduler.getQueue().clear();
		scheduler.getQueue().addAll(temp);
		event.reply(new SpuppybotComment("삭제 완료", SpuppybotColor.green).get().build());
		}
}
