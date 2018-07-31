package com.anbang.qipai.ruianmajiang.web.vo;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;

public class GamePlayerVO {
	private String playerId;
	private String nickname;
	private String headimgurl;
	private String state;
	private String onlineState;

	public GamePlayerVO(GamePlayerDbo dbo) {
		playerId = dbo.getPlayerId();
		nickname = dbo.getNickname();
		headimgurl = dbo.getHeadimgurl();
		state = dbo.getState().name();
		onlineState = dbo.getOnlineState().name();
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(String onlineState) {
		this.onlineState = onlineState;
	}

}
