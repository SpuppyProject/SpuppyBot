package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.github.yeoj34760.spuppybot.Music.Command.List.ListPrint;
import com.github.yeoj34760.spuppybot.Music.Command.List.Playing;
import com.github.yeoj34760.spuppybot.Music.Command.List.PlayingAndList;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class ListCommand extends Command {
	final MyGuild myGuild;

	public ListCommand(MyGuild myGuild) {
		this.myGuild = myGuild;
		this.name = "����Ʈ";
		this.aliases = new String[] { "list", "리스트", "모음집"};
		this.help = "����Ʈ Ȯ���մϴ�.";
	}

	@Override
	protected void execute(CommandEvent event) {
		TrackScheduler scheduler = myGuild.getGuildAudioPlayer(event.getGuild()).scheduler;
		ListPrint listPrint;
		if (!scheduler.getQueue().isEmpty() && scheduler.getPlayer().getPlayingTrack() != null) {
			listPrint = new PlayingAndList();
			listPrint.start(event, scheduler);
		} else if (scheduler.getPlayer().getPlayingTrack() != null) {
			listPrint = new Playing();
			listPrint.start(event, scheduler);
		} else {
			event.reply(new EmbedBuilder().setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128)
					.setTitle("없음").build());
		}
	}
}
