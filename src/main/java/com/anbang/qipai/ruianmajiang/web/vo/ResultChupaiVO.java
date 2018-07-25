package com.anbang.qipai.ruianmajiang.web.vo;

import com.dml.majiang.MajiangPai;

public class ResultChupaiVO {

	private MajiangPai pai;
	private boolean hupai;

	public ResultChupaiVO(MajiangPai pai, boolean hupai) {
		this.pai = pai;
		this.hupai = hupai;
	}

	public MajiangPai getPai() {
		return pai;
	}

	public void setPai(MajiangPai pai) {
		this.pai = pai;
	}

	public boolean isHupai() {
		return hupai;
	}

	public void setHupai(boolean hupai) {
		this.hupai = hupai;
	}

}
