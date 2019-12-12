package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.Music.GuildMusicManager;
import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.github.yeoj34760.spuppybot.utility.SpuppybotColor;
import com.github.yeoj34760.spuppybot.utility.SpuppybotComment;
import com.github.yeoj34760.spuppybot.utility.SpuppybotUri;
import com.github.yeoj34760.spuppybot.utility.Video;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public abstract class MusicStart {

	MyGuild myGuild;

	public MusicStart(MyGuild myGuild) {
		this.myGuild = myGuild;
	}

	public abstract void result(CommandEvent event);

	protected void start(CommandEvent event, String uri) {
		VoiceChannel channel = event.getGuild()
				.getVoiceChannelsByName(event.getMember().getVoiceState().getChannel().getName(), true).get(0);// 유저가
																												// 들어와
																												// 있는 정보
		GuildMusicManager musicManager = myGuild.getGuildAudioPlayer(channel.getGuild());
		myGuild.playerManager.registerSourceManager(new YoutubeAudioSourceManager());
		myGuild.playerManager.loadItemOrdered(musicManager, uri, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				State state;
				TrackScheduler scheduler = myGuild.getGuildAudioPlayer(event.getGuild()).scheduler;
				if (scheduler.getPlayer().getPlayingTrack() == null) // 재생중이지 않을 경우
				{
					state = new NoPlaying();
					event.reply(state.start(track).build());
				} else {
					state = new Playing();
					event.reply(state.start(track).build());
				}
				play(event.getGuild(), musicManager, track, event.getMember());
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				AudioTrack firstTrack = playlist.getSelectedTrack();
				EmbedBuilder builder = new EmbedBuilder();

				if (firstTrack == null) {
					builder.setDescription(playlist.getName());
					connectToFirstVoiceChannel(event.getGuild().getAudioManager(), event.getMember()); // 자동 음성방에 들어옴
					for (int i = 0; i <= 5; i++) {
						System.out.println(playlist.getTracks().get(i).getInfo().title);
						musicManager.scheduler.queue(playlist.getTracks().get(i)); // 플레이리스트 추가합니다.
					}
				} else {
					builder.setDescription(firstTrack.getInfo().title);
					play(event.getGuild(), musicManager, firstTrack, event.getMember());
				}

				Video video = new Video();
				builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
				builder.setTitle("추가 됨!");

				builder.setThumbnail(video.thumbnail(video.Id(playlist.getTracks().get(0).getInfo().uri)));
				builder.setFooter(Long.toString(playlist.getTracks().get(0).getInfo().length));
				builder.setColor(SpuppybotColor.green);
				event.getChannel().sendMessage(builder.build()).queue();

			}

			@Override
			public void noMatches() {
				event.reply(new SpuppybotComment("찾을 수 없음", SpuppybotColor.red).get().build());

			}

			@Override
			public void loadFailed(FriendlyException exception) {
				event.reply(new SpuppybotComment("불러오는 데 오류가 발생했습니다.", SpuppybotColor.red).get().build());
			}
		});
	}

	void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
		connectToFirstVoiceChannel(guild.getAudioManager(), member); // 자동 음성방에 들어옴
		musicManager.scheduler.queue(track); // 플레이리스트 추가합니다.

	}

	void connectToFirstVoiceChannel(AudioManager audioManager, Member member) {
		if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
			audioManager.openAudioConnection(
					audioManager.getGuild().getVoiceChannelById(member.getVoiceState().getChannel().getIdLong()));
		}
	}
}
