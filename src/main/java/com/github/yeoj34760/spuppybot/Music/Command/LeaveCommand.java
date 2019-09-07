package com.github.yeoj34760.spuppybot.Music.Command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class LeaveCommand extends Command {
	public LeaveCommand() {
		this.name = "나가";
		this.aliases = new String[] { "나가", "leave", "나감" };
		this.help = "음성 방에서 나갑니다.";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		event.getGuild().getAudioManager().closeAudioConnection();
		event.reply(new EmbedBuilder().setAuthor("SpuppyBot", "https://github.com/yeoj34760", "https://yeoj34760.github.io/Spuppybot/Spuppybotlogo_128.png").setTitle("나갔습니다.").build());
	}
}
