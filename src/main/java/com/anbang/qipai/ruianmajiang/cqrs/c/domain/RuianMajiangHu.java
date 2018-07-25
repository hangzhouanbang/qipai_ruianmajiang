package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.Hu;
import com.dml.majiang.MajiangPlayerAction;
import com.dml.majiang.ShoupaiPaiXing;

public class RuianMajiangHu implements Hu {

	private ShoupaiPaiXing shoupaiPaiXing;

	private RuianMajiangHushu hushu;

	// 吃碰杠胡要保存action
	private MajiangPlayerAction chipenggangAction;

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

	public MajiangPlayerAction getChipenggangAction() {
		return chipenggangAction;
	}

	public void setChipenggangAction(MajiangPlayerAction chipenggangAction) {
		this.chipenggangAction = chipenggangAction;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

}
