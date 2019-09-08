package com.github.yeoj34760.spuppybot.Music.Command.Play;

import com.github.yeoj34760.spuppybot.Music.GuildMusicManager;
import com.github.yeoj34760.spuppybot.Music.MyGuild;
import com.github.yeoj34760.spuppybot.Music.TrackScheduler;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

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
				.getVoiceChannelsByName(event.getMember().getVoiceState().getChannel().getName(), true).get(0);// 유저가 들어와 있는 음성방 정보
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
				if (firstTrack == null) {
					firstTrack = playlist.getTracks().get(0);
				}

				event.getChannel().sendMessage("음악을 재생합니다." + firstTrack.getInfo().title + " (first track of playlist "
						+ playlist.getName() + ")").queue();

				play(event.getGuild(), musicManager, firstTrack, event.getMember());
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
	}

	void play(Guild guild, GuildMusicManager musicManager, AudioTrack track, Member member) {
		connectToFirstVoiceChannel(guild.getAudioManager(), member); //자동 음성방에 들어옴
		musicManager.scheduler.queue(track); //플레이리스트 추가합니다.

	}

	void connectToFirstVoiceChannel(AudioManager audioManager, Member member) {
		if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
			audioManager.openAudioConnection(
					audioManager.getGuild().getVoiceChannelById(member.getVoiceState().getChannel().getIdLong()));
		}
	}
}
