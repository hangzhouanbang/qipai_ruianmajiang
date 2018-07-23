package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Hu;
import com.dml.majiang.ShoupaiPaiXing;

public class RuianMajiangHu implements Hu {

	private ShoupaiPaiXing shoupaiPaiXing;

	private RuianMajiangHushu hushu;

	public RuianMajiangHu() {
	}

	public RuianMajiangHu(ShoupaiPaiXing shoupaiPaiXing, RuianMajiangHushu hushu) {
		this.shoupaiPaiXing = shoupaiPaiXing;
		this.hushu = hushu;
	}

	public ShoupaiPaiXing getShoupaiPaiXing() {
		return shoupaiPaiXing;
	}

	public void setShoupaiPaiXing(ShoupaiPaiXing shoupaiPaiXing) {
		this.shoupaiPaiXing = shoupaiPaiXing;
	}

	public RuianMajiangHushu getHushu() {
		return hushu;
	}

	public void setHushu(RuianMajiangHushu hushu) {
		this.hushu = hushu;
	}

}
