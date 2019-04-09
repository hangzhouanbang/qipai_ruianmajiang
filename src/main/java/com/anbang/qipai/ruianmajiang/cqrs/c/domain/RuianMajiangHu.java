package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.player.Hu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangHu extends Hu {

	private RuianMajiangPanPlayerScore score;

	private boolean huxingHu;// 三财神推倒就不是胡形的胡

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.objToByteBuffer(getShoupaiPaiXing(), bb);
		ByteBufferSerializer.booleanToByteBuffer(isZimo(), bb);
		ByteBufferSerializer.booleanToByteBuffer(isDianpao(), bb);
		ByteBufferSerializer.stringToByteBuffer(getDianpaoPlayerId(), bb);
		ByteBufferSerializer.booleanToByteBuffer(isQianggang(), bb);
		ByteBufferSerializer.objToByteBuffer(score, bb);
		ByteBufferSerializer.booleanToByteBuffer(huxingHu, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		setShoupaiPaiXing(ByteBufferSerializer.byteBufferToObj(bb));
		setZimo(ByteBufferSerializer.byteBufferToBoolean(bb));
		setDianpao(ByteBufferSerializer.byteBufferToBoolean(bb));
		setDianpaoPlayerId(ByteBufferSerializer.byteBufferToString(bb));
		setQianggang(ByteBufferSerializer.byteBufferToBoolean(bb));
		score = ByteBufferSerializer.byteBufferToObj(bb);
		huxingHu = ByteBufferSerializer.byteBufferToBoolean(bb);
	}

	public RuianMajiangHu() {
	}

	public RuianMajiangHu(ShoupaiPaiXing shoupaiPaiXing, RuianMajiangPanPlayerScore score) {
		super(shoupaiPaiXing);
		this.score = score;
		this.huxingHu = true;
	}

	public RuianMajiangHu(RuianMajiangPanPlayerScore score) {
		this.score = score;
		this.huxingHu = false;
	}

	public RuianMajiangPanPlayerScore getScore() {
		return score;
	}

	public void setScore(RuianMajiangPanPlayerScore score) {
		this.score = score;
	}

	public boolean isHuxingHu() {
		return huxingHu;
	}

	public void setHuxingHu(boolean huxingHu) {
		this.huxingHu = huxingHu;
	}

}
