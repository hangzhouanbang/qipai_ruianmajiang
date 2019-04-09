package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

public class MajiangGamePlayerDbo implements ByteBufferAble {
	private String playerId;
	private String nickname;
	private String gender;// 会员性别:男:male,女:female
	private String headimgurl;
	private String state;// 原来是 joined, readyToStart, playing, panFinished, finished
	private GamePlayerOnlineState onlineState;
	private int totalScore;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(playerId, bb);
		ByteBufferSerializer.stringToByteBuffer(nickname, bb);
		ByteBufferSerializer.stringToByteBuffer(gender, bb);
		ByteBufferSerializer.stringToByteBuffer(headimgurl, bb);
		ByteBufferSerializer.stringToByteBuffer(state, bb);
		bb.put((byte) onlineState.ordinal());
		bb.putInt(totalScore);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		playerId = ByteBufferSerializer.byteBufferToString(bb);
		nickname = ByteBufferSerializer.byteBufferToString(bb);
		gender = ByteBufferSerializer.byteBufferToString(bb);
		headimgurl = ByteBufferSerializer.byteBufferToString(bb);
		state = ByteBufferSerializer.byteBufferToString(bb);
		onlineState = GamePlayerOnlineState.valueOf(bb.get());
		totalScore = bb.getInt();
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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
