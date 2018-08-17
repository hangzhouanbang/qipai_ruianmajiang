package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class RuianMajiangHu implements Hu {

	private ShoupaiPaiXing shoupaiPaiXing;

	private RuianMajiangPanPlayerScore score;

	private boolean zimo;

	private boolean dianpao;

	private boolean qianggang;

	private boolean huxingHu;// 三财神推倒就不是胡形的胡

	private String dianpaoPlayerId;

	public RuianMajiangHu() {
	}

	public RuianMajiangHu(ShoupaiPaiXing shoupaiPaiXing, RuianMajiangPanPlayerScore score) {
		this.shoupaiPaiXing = shoupaiPaiXing;
		this.score = score;
		this.huxingHu = true;
	}

	public RuianMajiangHu(RuianMajiangPanPlayerScore score) {
		this.score = score;
		this.huxingHu = false;
	}

	public ShoupaiPaiXing getShoupaiPaiXing() {
		return shoupaiPaiXing;
	}

	public void setShoupaiPaiXing(ShoupaiPaiXing shoupaiPaiXing) {
		this.shoupaiPaiXing = shoupaiPaiXing;
	}

	public RuianMajiangPanPlayerScore getScore() {
		return score;
	}

	public void setScore(RuianMajiangPanPlayerScore score) {
		this.score = score;
	}

	public boolean isZimo() {
		return zimo;
	}

	public void setZimo(boolean zimo) {
		this.zimo = zimo;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public void setDianpao(boolean dianpao) {
		this.dianpao = dianpao;
	}

	public boolean isQianggang() {
		return qianggang;
	}

	public void setQianggang(boolean qianggang) {
		this.qianggang = qianggang;
	}

	public boolean isHuxingHu() {
		return huxingHu;
	}

	public void setHuxingHu(boolean huxingHu) {
		this.huxingHu = huxingHu;
	}

	public String getDianpaoPlayerId() {
		return dianpaoPlayerId;
	}

	public void setDianpaoPlayerId(String dianpaoPlayerId) {
		this.dianpaoPlayerId = dianpaoPlayerId;
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
