package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.PanResult;

public class RuianMajiangPanResult implements PanResult {

	private List<RuianMajiangPanPlayerResult> playerResultList;

	public List<RuianMajiangPanPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

}
