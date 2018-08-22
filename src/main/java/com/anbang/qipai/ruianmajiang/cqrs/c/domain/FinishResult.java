package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.mpgame.game.GameValueObject;

public class FinishResult {
	private GameValueObject gameValueObject;
	private RuianMajiangJuResult juResult;

	public GameValueObject getGameValueObject() {
		return gameValueObject;
	}

	public void setGameValueObject(GameValueObject gameValueObject) {
		this.gameValueObject = gameValueObject;
	}

	public RuianMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(RuianMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
