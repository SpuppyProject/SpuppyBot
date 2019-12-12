package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
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
		event.reply(new SpuppybotComment("나갑니다.", SpuppybotColor.blue).get().build());
	}
}
