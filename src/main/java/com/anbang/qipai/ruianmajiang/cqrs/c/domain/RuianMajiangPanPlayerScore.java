package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangPanPlayerScore {

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

	public void jiesuan() {
		if (pao != null) {
			paoScore = pao.getValue() * 10;
		}
		jiesuanScore = jiesuanHushu + jiesuanPao * 10;
	}

	public void jiesuanHushu(int delta) {
		jiesuanHushu += delta;
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

}
