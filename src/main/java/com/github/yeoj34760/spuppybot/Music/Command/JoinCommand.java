package com.github.yeoj34760.spuppybot.Music.Command;

import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class JoinCommand extends Command {

	public JoinCommand() {
		this.name = "들어와";
		this.aliases = new String[] { "들어와", "join", "들어오기" };
		this.help = "스퍼피봇이 음성 방에 들어옵니다.";
	}
	
	@Override
	protected void execute(CommandEvent event) {
		if(!event.getMember().getVoiceState().inVoiceChannel())
		{
			event.reply("음성 방에 들어와주세요.");
			return;
		}
		event.getGuild().getAudioManager().openAudioConnection(event.getGuild().getVoiceChannelById(event.getMember().getVoiceState().getChannel().getIdLong()));
		event.reply(new SpuppybotComment("들어옵니다.", SpuppybotColor.green).get().build());
	}
}
