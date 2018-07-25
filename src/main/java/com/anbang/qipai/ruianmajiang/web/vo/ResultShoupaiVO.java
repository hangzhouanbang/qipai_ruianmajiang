package com.anbang.qipai.ruianmajiang.web.vo;

import com.dml.majiang.MajiangPai;

public class ResultShoupaiVO {

	private MajiangPai pai;
	private boolean caishen;
	private boolean hupai;

	public MajiangPai getPai() {
		return pai;
	}

	public void setPai(MajiangPai pai) {
		this.pai = pai;
	}

	public boolean isCaishen() {
		return caishen;
	}

	public void setCaishen(boolean caishen) {
		this.caishen = caishen;
	}

	public boolean isHupai() {
		return hupai;
	}

	public void setHupai(boolean hupai) {
		this.hupai = hupai;
	}

}
