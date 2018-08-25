package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.pan.frame.PanActionFrame;

public class MajiangActionResult {

	private MajiangGameValueObject majiangGame;
	private PanActionFrame panActionFrame;
	private RuianMajiangPanResult panResult;
	private RuianMajiangJuResult juResult;

	public MajiangGameValueObject getMajiangGame() {
		return majiangGame;
	}

	public void setMajiangGame(MajiangGameValueObject majiangGame) {
		this.majiangGame = majiangGame;
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
