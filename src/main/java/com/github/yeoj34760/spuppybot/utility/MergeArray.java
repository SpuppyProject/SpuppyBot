package com.github.yeoj34760.spuppybot.utility;

public class MergeArray {
	public static String Message(String[] s) {
		String message = "";
		for (int i = 1; i < s.length; i++) {
			message += s[i] + ' ';
		}
		return message;
	}
}
