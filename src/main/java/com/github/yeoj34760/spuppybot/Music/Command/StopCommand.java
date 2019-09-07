package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class StopCommand extends Command {
	final MyGuild myGuild;

	public StopCommand(MyGuild myGuild) {
		this.myGuild = myGuild;
		this.name = "중지";
		this.aliases = new String[] { "stop", "중지", "멈춰" };
		this.help = "음악을 중지합니다.";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		myGuild.getGuildAudioPlayer(event.getGuild()).player.stopTrack();
		event.reply(new EmbedBuilder().setAuthor("SpuppyBot", "https://github.com/yeoj34760", "https://yeoj34760.github.io/Spuppybot/Spuppybotlogo_128.png").setTitle("음악을 중지합니다.").build());
	}
}