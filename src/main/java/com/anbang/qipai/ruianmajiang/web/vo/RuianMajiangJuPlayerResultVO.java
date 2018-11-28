package com.anbang.qipai.ruianmajiang.web.vo;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class RuianMajiangJuPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private int huCount;
	private int caishenCount;
	private int dapaoCount;
	private int maxTaishu;
	private int maxHushu;
	private int totalScore;

	public RuianMajiangJuPlayerResultVO(RuianMajiangJuPlayerResult juPlayerResult,
			MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = juPlayerResult.getHuCount();
		caishenCount = juPlayerResult.getCaishenCount();
		dapaoCount = juPlayerResult.getDapaoCount();
		maxTaishu = juPlayerResult.getMaxTaishu();
		maxHushu = juPlayerResult.getMaxHushu();
		totalScore = juPlayerResult.getTotalScore();
	}

	public RuianMajiangJuPlayerResultVO(MajiangGamePlayerDbo majiangGamePlayerDbo) {
		playerId = majiangGamePlayerDbo.getPlayerId();
		nickname = majiangGamePlayerDbo.getNickname();
		headimgurl = majiangGamePlayerDbo.getHeadimgurl();
		huCount = 0;
		caishenCount = 0;
		dapaoCount = 0;
		maxTaishu = 0;
		maxHushu = 0;
		totalScore = 0;
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public int getHuCount() {
		return huCount;
	}

	public int getCaishenCount() {
		return caishenCount;
	}

	public int getDapaoCount() {
		return dapaoCount;
	}

	public int getMaxTaishu() {
		return maxTaishu;
	}

	public void setMaxTaishu(int maxTaishu) {
		this.maxTaishu = maxTaishu;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public void setHuCount(int huCount) {
		this.huCount = huCount;
	}

	public void setCaishenCount(int caishenCount) {
		this.caishenCount = caishenCount;
	}

	public void setDapaoCount(int dapaoCount) {
		this.dapaoCount = dapaoCount;
	}

	public void setMaxHushu(int maxHushu) {
		this.maxHushu = maxHushu;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getMaxHushu() {
		return maxHushu;
	}

	public int getTotalScore() {
		return totalScore;
	}

}
