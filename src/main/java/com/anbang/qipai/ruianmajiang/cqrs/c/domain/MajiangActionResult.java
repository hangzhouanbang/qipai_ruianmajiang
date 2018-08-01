package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pan.PanActionFrame;
import com.dml.mpgame.GameValueObject;

public class MajiangActionResult {
	private GameValueObject game;
	private PanActionFrame panActionFrame;
	private RuianMajiangPanResult panResult;
	private List<String> otherPlayerIds = new ArrayList<>();

	public GameValueObject getGame() {
		return game;
	}

	public void setGame(GameValueObject game) {
		this.game = game;
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

	public List<String> getOtherPlayerIds() {
		return otherPlayerIds;
	}

	public void setOtherPlayerIds(List<String> otherPlayerIds) {
		this.otherPlayerIds = otherPlayerIds;
	}

}
