package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GameValueObject;

public class MajiangActionResult {

	private GameValueObject gameValueObject;
	private PanActionFrame panActionFrame;
	private RuianMajiangPanResult panResult;
	private RuianMajiangJuResult juResult;

	public GameValueObject getGameValueObject() {
		return gameValueObject;
	}

	public void setGameValueObject(GameValueObject gameValueObject) {
		this.gameValueObject = gameValueObject;
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

}
