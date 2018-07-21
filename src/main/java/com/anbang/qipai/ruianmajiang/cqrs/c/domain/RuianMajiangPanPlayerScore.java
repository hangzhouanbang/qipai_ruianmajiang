package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangPanPlayerScore {
	private RuianMajiangPao pao;
	private RuianMajiangHushu hushu;
	private int value;

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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
