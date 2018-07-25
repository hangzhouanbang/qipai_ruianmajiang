package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.PanResult;

public class RuianMajiangPanResult extends PanResult {

	private String zhuangPlayerId;

	private List<RuianMajiangPanPlayerResult> playerResultList;

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
	}

	public List<RuianMajiangPanPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

}
