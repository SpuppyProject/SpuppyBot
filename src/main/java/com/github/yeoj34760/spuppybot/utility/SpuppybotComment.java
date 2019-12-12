package com.github.yeoj34760.spuppybot.utility;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;

public class SpuppybotComment {
	EmbedBuilder builder;

	public SpuppybotComment(String Title, String TitleUri, String Contest, Color Color, String Uri, String Footer) {
		builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle(Title, TitleUri);
		builder.setDescription(Contest);
		builder.setThumbnail(Uri);
		builder.setFooter(Footer);
		builder.setColor(Color);
	}

	public SpuppybotComment(String Title, String TitleUri, String Contest, Color Color, String Footer) {
		builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle(Title);
		builder.setDescription(Contest);
		builder.setFooter(Footer);
		builder.setColor(Color);
	}

	public SpuppybotComment(String Title, String TitleUri, String Contest, Color Color) {
		builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setDescription(Contest);
		builder.setTitle(Title, TitleUri);
		builder.setColor(Color);
	}

	public SpuppybotComment(String Title,String TitleUri, Color Color) {
		builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle(Title, TitleUri);
		builder.setColor(Color);
	}
	public SpuppybotComment(String Title, Color Color) {
		builder = new EmbedBuilder();
		builder.setAuthor("SpuppyBot", SpuppybotUri.Github, SpuppybotUri.Icon_128);
		builder.setTitle(Title);
		builder.setColor(Color);
	}
	public EmbedBuilder get() {
		return builder;
	}
}
