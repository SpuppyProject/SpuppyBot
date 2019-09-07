package com.github.yeoj34760.spuppybot.Music.Command;

import java.awt.Color;
import java.util.concurrent.TimeUnit;


import com.github.yeoj34760.spuppybot.Music.GuildMusicManager;
import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.utility.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeSearchProvider;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.AudioManager;

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
		Guild guild = event.getGuild();
		VoiceChannel channel = guild
				.getVoiceChannelsByName(event.getMember().getVoiceState().getChannel().getName(), true).get(0);
		GuildMusicManager musicManager = myGuild.getGuildAudioPlayer(channel.getGuild());
		YoutubeSearchProvider searchProvider = new YoutubeSearchProvider(new YoutubeAudioSourceManager());
		String message = MergeArray.Message(event.getMessage().getContentRaw().split(" ", 100));
		AudioPlaylist track = (AudioPlaylist) searchProvider.loadSearchResult(message);
		String text = "";
		for (int i = 1; i <= 5; i++) {
			text += "**[" + i + "]**" + " " + track.getTracks().get(i - 1).getInfo().title + '\n';
		}
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle("번호를 선택하세요", null);
		embed.setAuthor("SpuppyBot", SpuppybotUri.Github,
				SpuppybotUri.Icon_128);
		embed.setColor(Color.blue);
		embed.setDescription(text);
		event.reply(embed.build());
		waiter.waitForEvent(MessageReceivedEvent.class,
				// make sure it's by the same user, and in the same channel, and for safety, a
				// different message
				e -> e.getAuthor().equals(event.getAuthor()) && e.getChannel().equals(event.getChannel())
						&& !e.getMessage().equals(event.getMessage()),
				// respond, inserting the name they listed into the response
				e -> {
					myGuild.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
					myGuild.playerManager.loadItemOrdered(musicManager,
							track.getTracks().get(Integer.parseInt(e.getMessage().getContentRaw()) - 1).getInfo().uri,
							new AudioLoadResultHandler() {
								@Override
								public void trackLoaded(AudioTrack track) {
									Video video = new Video();
									event.reply(new EmbedBuilder()
											.setAuthor("SpuppyBot", SpuppybotUri.Github,
													SpuppybotUri.Icon_128)
											.setTitle(track.getInfo().title).setThumbnail(video.thumbnail(video.Id(track.getInfo().uri)))
											.build());
									play(channel.getGuild(), musicManager, track, event.getMember());
								}

								@Override
								public void playlistLoaded(AudioPlaylist playlist) { // �÷��̸���Ʈ�� ��
									AudioTrack firstTrack = playlist.getSelectedTrack();
									if (firstTrack == null) {
										firstTrack = playlist.getTracks().get(0);
									}

									event.getChannel().sendMessage("음악을 재생합니다." + firstTrack.getInfo().title
											+ " (first track of playlist " + playlist.getName() + ")").queue();

									play(channel.getGuild(), musicManager, firstTrack, event.getMember());
								}

								@Override
								public void noMatches() {
									event.getChannel().sendMessage("찾을 수가 읎어요 ").queue();
								}

								@Override
								public void loadFailed(FriendlyException exception) {
									event.getChannel().sendMessage("불러오는데 오류가 발생했습니다. " + exception.getMessage()).queue();
								}
							});
				},
				// if the user takes more than a minute, time out
				1, TimeUnit.MINUTES, () -> {
					event.reply("�ð� �ʰ���.");
					return;
				});

	}

	private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
		connectToFirstVoiceChannel(guild.getAudioManager(), member);
		musicManager.scheduler.queue(track); // ����� �մϴ�?

	}

	private static void connectToFirstVoiceChannel(AudioManager audioManager, Member member) {
		if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) { // ���� ����Ǿ� �ִ��� Ȯ���մϴ�.
			audioManager.openAudioConnection(
					audioManager.getGuild().getVoiceChannelById(member.getVoiceState().getChannel().getIdLong()));
		}
	}
}
