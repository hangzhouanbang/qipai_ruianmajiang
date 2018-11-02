package com.anbang.qipai.ruianmajiang.msg.msjobj;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.RuianMajiangPanPlayerResultDbo;

public class RuianMajiangPanPlayerResultMO {
	private String id;// 玩家id
	private String nickname;// 玩家昵称
	private int score;// 一盘总分

	public RuianMajiangPanPlayerResultMO(MajiangGamePlayerDbo gamePlayerDbo,
			RuianMajiangPanPlayerResultDbo panPlayerResult) {
		id = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		score = panPlayerResult.getPlayerResult().getScore().getJiesuanScore();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
