package com.anbang.qipai.ruianmajiang.web.vo;

import com.dml.majiang.GuipaiDangPai;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.ShoupaiJiesuanPai;

public class ResultShoupaiVO {

	private MajiangPai pai;
	private boolean caishen;
	private boolean hupai;

	public ResultShoupaiVO(ShoupaiJiesuanPai shoupaiJiesuanPai) {
		pai = shoupaiJiesuanPai.getYuanPaiType();
		hupai = shoupaiJiesuanPai.isLastActionPai();
		caishen = shoupaiJiesuanPai.getDangType().equals(GuipaiDangPai.dangType);
	}

	public ResultShoupaiVO(MajiangPai pai, boolean caishen) {
		this.pai = pai;
		this.caishen = caishen;
		hupai = false;
	}

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
