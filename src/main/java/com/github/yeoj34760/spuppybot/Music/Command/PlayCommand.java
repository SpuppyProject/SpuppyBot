package com.github.yeoj34760.spuppybot.Music.Command;

import java.net.MalformedURLException;
import java.net.URL;
import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.Command.Play.*;
import com.github.yeoj34760.spuppybot.utility.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;


public class PlayCommand extends Command {
	private final EventWaiter waiter;
	final MyGuild myGuild;

	public PlayCommand(EventWaiter waiter, MyGuild myGuild) {
		this.myGuild = myGuild;
		this.waiter = waiter;
		this.name = "플레이";
		this.aliases = new String[] { "play", "플레이" };
		this.help = "유튜브이용하여 음악을 재생합니다";
	}

	@Override
	protected void execute(CommandEvent event) {
		// ask what the user's name is
		try {
			String message = MergeArray.Message(event.getMessage().getContentRaw().split(" ", 100)).trim(); //명령어 문자를 제외합니다.
			new URL(message); //유효한 URI인지 확인합니다.
			MusicStart musicStart = new Uri(myGuild, message); //Uri 클래스 생성합니다.
			musicStart.result(event); //음악 재생합니다.
			
		} catch (MalformedURLException e) { //유효한 URI아니라면.
			MusicStart musicStart = new Search(myGuild, waiter); //Search 클래스 생성합니다.
			musicStart.result(event); //음악 재생합니다.
		}

	}

}
