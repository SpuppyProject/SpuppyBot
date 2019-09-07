package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import net.dv8tion.jda.api.EmbedBuilder;

public class PauseCommand extends Command {
	final MyGuild myGuild;

	public PauseCommand(MyGuild myGuild) {
		this.myGuild = myGuild;
		this.name = "일시중지";
		this.aliases = new String[] { "일시중지", "Pause" };
		this.help = "음악을 일시중지합니다.";
	}

	@Override
	protected void execute(CommandEvent event) {
		String message = "";
		AudioPlayer player = myGuild.getGuildAudioPlayer(event.getGuild()).player;
		if (player.getPlayingTrack() == null) //재생 중이지 않을 경우
			message = "현재 재생하지 않습니다.";
		else if (!player.isPaused()) { //일시중지 비활성화일 경우
			player.setPaused(true);
			message = "일시중지합니다.";
		} else { //일시중지 활성화일 경우
			player.setPaused(false);
			message = "일시중지 해체합니다.";
		}
		event.reply(new EmbedBuilder().setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128)
				.setTitle(message).build());

	}
}
