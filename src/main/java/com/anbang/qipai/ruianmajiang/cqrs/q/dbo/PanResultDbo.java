package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;

public class PanResultDbo {
	private String id;
	private String gameId;
	private int panNo;
	private List<RuianMajiangPanPlayerResult> playerResultList;

	public PanResultDbo() {
	}

	public PanResultDbo(String gameId, RuianMajiangPanResult ruianMajiangPanResult) {
		this.gameId = gameId;
		panNo = ruianMajiangPanResult.getPanNo();
		playerResultList = new ArrayList<>(ruianMajiangPanResult.getPlayerResultList());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public List<RuianMajiangPanPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

}
