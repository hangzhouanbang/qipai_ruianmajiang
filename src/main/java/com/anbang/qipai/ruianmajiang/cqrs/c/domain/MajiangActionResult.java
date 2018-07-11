package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.PanActionFrame;
import com.dml.mpgame.GameValueObject;

public class MajiangActionResult {
	private GameValueObject game;
	private PanActionFrame panActionFrame;

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

}
