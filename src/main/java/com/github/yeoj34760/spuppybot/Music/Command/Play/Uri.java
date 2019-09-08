package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.jagrosh.jdautilities.command.CommandEvent;

public class Uri extends MusicStart {
	String uri;
	public Uri(MyGuild myGuild, String uri) {
		super(myGuild);
		this.uri = uri;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void result(CommandEvent event) {
		start(event, uri);
	}

}
