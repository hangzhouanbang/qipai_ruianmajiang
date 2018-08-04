package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import com.dml.mpgame.GamePlayerOnlineState;

public class MajiangGamePlayerDbo {

	private String id;
	private String playerId;
	private String gameId;
	private String nickname;
	private String headimgurl;
	private MajiangGamePlayerState state;
	private GamePlayerOnlineState onlineState;
	private int totalScore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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

	public MajiangGamePlayerState getState() {
		return state;
	}

	public void setState(MajiangGamePlayerState state) {
		this.state = state;
	}

	public GamePlayerOnlineState getOnlineState() {
		return onlineState;
	}

	public void setOnlineState(GamePlayerOnlineState onlineState) {
		this.onlineState = onlineState;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
