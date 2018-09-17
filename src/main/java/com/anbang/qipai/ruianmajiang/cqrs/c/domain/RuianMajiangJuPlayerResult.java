package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangJuPlayerResult {

	private String playerId;
	private int huCount;
	private int caishenCount;
	private int dapaoCount;
	private int maxHushu;
	private int totalScore;

	public void increaseHuCount() {
		huCount++;
	}

	public void increaseCaishenCount(int amount) {
		caishenCount += amount;
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
