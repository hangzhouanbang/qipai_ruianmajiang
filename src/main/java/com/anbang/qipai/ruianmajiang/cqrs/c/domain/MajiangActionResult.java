package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.mpgame.GameValueObject;

public class MajiangActionResult {
	private GameValueObject game;
	private byte[] actionFrameDataAfterAction;

	public GameValueObject getGame() {
		return game;
	}

	public void setGame(GameValueObject game) {
		this.game = game;
	}

	public byte[] getActionFrameDataAfterAction() {
		return actionFrameDataAfterAction;
	}

	public void setActionFrameDataAfterAction(byte[] actionFrameDataAfterAction) {
		this.actionFrameDataAfterAction = actionFrameDataAfterAction;
	}

}
