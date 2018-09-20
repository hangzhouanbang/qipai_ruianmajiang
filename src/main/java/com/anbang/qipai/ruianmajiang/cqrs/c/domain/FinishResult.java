package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class FinishResult {
	private MajiangGameValueObject majiangGameValueObject;
	private RuianMajiangJuResult juResult;

	public MajiangGameValueObject getMajiangGameValueObject() {
		return majiangGameValueObject;
	}

	public void setMajiangGameValueObject(MajiangGameValueObject majiangGameValueObject) {
		this.majiangGameValueObject = majiangGameValueObject;
	}

	public RuianMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(RuianMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
