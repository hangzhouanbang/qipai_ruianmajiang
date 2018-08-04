package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pan.frame.PanActionFrame;

public class MajiangActionResult {

	private String gameId;
	private PanActionFrame panActionFrame;
	private RuianMajiangPanResult panResult;
	private RuianMajiangJuResult juResult;
	private List<String> otherPlayerIds = new ArrayList<>();

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public RuianMajiangPanResult getPanResult() {
		return panResult;
	}

	public void setPanResult(RuianMajiangPanResult panResult) {
		this.panResult = panResult;
	}

	public RuianMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(RuianMajiangJuResult juResult) {
		this.juResult = juResult;
	}

	public List<String> getOtherPlayerIds() {
		return otherPlayerIds;
	}

	public void setOtherPlayerIds(List<String> otherPlayerIds) {
		this.otherPlayerIds = otherPlayerIds;
	}

}
