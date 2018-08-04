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
		maxHushu = juPlayerResult.getMaxHushu();
		totalScore = juPlayerResult.getTotalScore();
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

	public int getMaxHushu() {
		return maxHushu;
	}

	public int getTotalScore() {
		return totalScore;
	}

}
