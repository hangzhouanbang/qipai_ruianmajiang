package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangPanPlayerScore implements ByteBufferAble {

	private RuianMajiangPao pao;
	private int paoScore;
	private RuianMajiangHushu hushu;

	/**
	 * 有可能是负数
	 */
	private int jiesuanHushu;

	/**
	 * 有可能是负数
	 */
	private int jiesuanPao;

	private int jiesuanScore;

	private int value;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.objToByteBuffer(pao, bb);
		bb.putInt(paoScore);
		ByteBufferSerializer.objToByteBuffer(hushu, bb);
		bb.putInt(jiesuanHushu);
		bb.putInt(jiesuanPao);
		bb.putInt(jiesuanScore);
		bb.putInt(value);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		pao = ByteBufferSerializer.byteBufferToObj(bb);
		paoScore = bb.getInt();
		hushu = ByteBufferSerializer.byteBufferToObj(bb);
		jiesuanHushu = bb.getInt();
		jiesuanPao = bb.getInt();
		jiesuanScore = bb.getInt();
		value = bb.getInt();
	}

	public void jiesuan() {
		jiesuanScore = (jiesuanHushu / 10) + (jiesuanPao * 5);// TODO 一炮10或者5要配置
	}

	public void jiesuanHushu(int delta) {
		jiesuanHushu += delta;
	}

	public void jiesuanPao(RuianMajiangPao anotherPlayerPao) {
		if (pao != null) {
			jiesuanPao += pao.jiesuan(anotherPlayerPao);
		}
	}

	private int quzheng(int value) {
		int shang = value / 10;
		int yu = value % 10;
		if (yu > 0) {
			return (shang + 1) * 10;
		} else {
			return shang * 10;
		}
	}

	public void calculate() {
		if (pao != null) {
			paoScore = pao.getValue() * 10;
		}
		value = hushu.quzhengValue() / 10 + paoScore;
	}

	public RuianMajiangPao getPao() {
		return pao;
	}

	public void setPao(RuianMajiangPao pao) {
		this.pao = pao;
	}

	public RuianMajiangHushu getHushu() {
		return hushu;
	}

	public void setHushu(RuianMajiangHushu hushu) {
		this.hushu = hushu;
	}

	public int getPaoScore() {
		return paoScore;
	}

	public void setPaoScore(int paoScore) {
		this.paoScore = paoScore;
	}

	public int getJiesuanHushu() {
		return jiesuanHushu;
	}

	public void setJiesuanHushu(int jiesuanHushu) {
		this.jiesuanHushu = jiesuanHushu;
	}

	public int getJiesuanPao() {
		return jiesuanPao;
	}

	public void setJiesuanPao(int jiesuanPao) {
		this.jiesuanPao = jiesuanPao;
	}

	public int getJiesuanScore() {
		return jiesuanScore;
	}

	public void setJiesuanScore(int jiesuanScore) {
		this.jiesuanScore = jiesuanScore;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
