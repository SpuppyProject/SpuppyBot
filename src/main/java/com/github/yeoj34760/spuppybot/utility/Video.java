package com.github.yeoj34760.spuppybot.utility;

import java.net.URI;
import java.net.URISyntaxException;

public class Video {
	public String thumbnail(String VideoId) {
		return "https://img.youtube.com/vi/" + VideoId + "/0.jpg"; //썸네일 주소를 추출합니다.
	}

	public String Id(String uri) {
		String VideoId = null;
		try {
			VideoId = new URI(uri).getRawQuery().replace("v=", ""); //비디오 아이디를 추출합니다.

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return VideoId;
	}
}
