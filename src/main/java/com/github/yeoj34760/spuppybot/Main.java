package com.github.yeoj34760.spuppybot;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.Command.*;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;

import net.dv8tion.jda.api.JDABuilder;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final MyGuild myGuild = new MyGuild();
		EventWaiter waiter = new EventWaiter();
		CommandClientBuilder client = new CommandClientBuilder();
		client.setOwnerId("243363817646456833");
		client.useDefaultGame();
		client.setEmojis("\uD83D\uDE03", "\uD83D\uDE2E", "\uD83D\uDE26");
		client.setPrefix("!");
		client.addCommands(new PlayCommand(waiter, myGuild));
		client.addCommands(new StopCommand(myGuild));
		client.addCommands(new SkipCommand(myGuild));
		client.addCommands(new JoinCommand());
		client.addCommands(new LeaveCommand());
		client.addCommands(new ListCommand(myGuild));
		new JDABuilder("토큰")
				.addEventListeners(waiter, client.build()).setAudioSendFactory(new NativeAudioSendFactory()).build();

	}

}
