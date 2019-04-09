package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangJuPlayerResult implements ByteBufferAble {

	private String playerId;
	private int huCount;
	private int caishenCount;
	private int dapaoCount;
	private int maxTaishu;
	private int maxHushu;
	private int totalScore;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(playerId, bb);
		bb.putInt(huCount);
		bb.putInt(caishenCount);
		bb.putInt(dapaoCount);
		bb.putInt(maxTaishu);
		bb.putInt(maxHushu);
		bb.putInt(totalScore);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		playerId = ByteBufferSerializer.byteBufferToString(bb);
		huCount = bb.getInt();
		caishenCount = bb.getInt();
		dapaoCount = bb.getInt();
		maxTaishu = bb.getInt();
		maxHushu = bb.getInt();
		totalScore = bb.getInt();
	}

	public void increaseHuCount() {
		huCount++;
	}

	public void increaseCaishenCount(int amount) {
		caishenCount += amount;
	}

	public void tryAndUpdateMaxTaishu(int taishu) {
		if (taishu > maxTaishu) {
			maxTaishu = taishu;
		}
	}

	public void tryAndUpdateMaxHushu(int hushu) {
		if (hushu > maxHushu) {
			maxHushu = hushu;
		}
	}

	public void increaseDapaoCount(int amount) {
		dapaoCount += amount;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getHuCount() {
		return huCount;
	}

	public void setHuCount(int huCount) {
		this.huCount = huCount;
	}

	public int getCaishenCount() {
		return caishenCount;
	}

	public void setCaishenCount(int caishenCount) {
		this.caishenCount = caishenCount;
	}

	public int getDapaoCount() {
		return dapaoCount;
	}

	public void setDapaoCount(int dapaoCount) {
		this.dapaoCount = dapaoCount;
	}

	public int getMaxTaishu() {
		return maxTaishu;
	}

	public void setMaxTaishu(int maxTaishu) {
		this.maxTaishu = maxTaishu;
	}

	public int getMaxHushu() {
		return maxHushu;
	}

	public void setMaxHushu(int maxHushu) {
		this.maxHushu = maxHushu;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
