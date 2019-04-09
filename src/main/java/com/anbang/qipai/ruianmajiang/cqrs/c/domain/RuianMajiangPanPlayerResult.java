package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.pan.result.PanPlayerResult;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangPanPlayerResult extends PanPlayerResult implements ByteBufferAble {

	private RuianMajiangPanPlayerScore score;

	private int totalScore;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(getPlayerId(), bb);
		ByteBufferSerializer.objToByteBuffer(score, bb);
		bb.putInt(totalScore);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		setPlayerId(ByteBufferSerializer.byteBufferToString(bb));
		score = ByteBufferSerializer.byteBufferToObj(bb);
		totalScore = bb.getInt();
	}

	public RuianMajiangPanPlayerResult() {
	}

	public RuianMajiangPanPlayerResult(String playerId) {
		super(playerId);
	}

	public RuianMajiangPanPlayerScore getScore() {
		return score;
	}

	public void setScore(RuianMajiangPanPlayerScore score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
