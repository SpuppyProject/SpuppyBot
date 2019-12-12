package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
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
		event.reply(new SpuppybotComment("음악을 중지합니다.", SpuppybotColor.blue).get().build());
		}
}