package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.PanActionFrame;
import com.dml.mpgame.GameValueObject;

public class MajiangActionResult {
	private GameValueObject game;
	private PanActionFrame panActionFrame;
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

	public List<String> getOtherPlayerIds() {
		return otherPlayerIds;
	}

	public void setOtherPlayerIds(List<String> otherPlayerIds) {
		this.otherPlayerIds = otherPlayerIds;
	}

}
