package com.anbang.qipai.ruianmajiang.web.vo;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangTaishu;

public class RuianMajiangTaishuVO {

	private boolean hongzhongPeng;
	private boolean hongzhongAnke;
	private boolean hongzhongGang;
	private boolean facaiPeng;
	private boolean facaiAnke;
	private boolean facaiGang;
	private boolean zuofengPeng;
	private boolean zuofengAnke;
	private boolean zuofengGang;
	private int baibanShu;
	private boolean danzhangdiaoHu;
	private boolean pingHu;
	private boolean qianggangHu;
	private boolean gangkaiHu;
	private boolean hunyiseHu;
	private boolean duiduiHu;
	private boolean sifengqiHu;
	private boolean sancaishenHu;
	private boolean tianHu;
	private boolean diHu;
	private boolean qingyiseHu;
	private boolean shuangCaishengHu;
	private int value;

	public RuianMajiangTaishuVO(RuianMajiangTaishu taishu) {
		hongzhongPeng = taishu.isHongzhongPeng();
		hongzhongAnke = taishu.isHongzhongAnke();
		hongzhongGang = taishu.isHongzhongGang();
		facaiPeng = taishu.isFacaiPeng();
		facaiAnke = taishu.isFacaiAnke();
		facaiGang = taishu.isFacaiGang();
		zuofengPeng = taishu.isZuofengPeng();
		zuofengAnke = taishu.isZuofengAnke();
		zuofengGang = taishu.isZuofengGang();
		baibanShu = taishu.getBaibanShu();
		danzhangdiaoHu = taishu.isDanzhangdiaoHu();
		pingHu = taishu.isPingHu();
		qianggangHu = taishu.isQianggangHu();
		gangkaiHu = taishu.isGangkaiHu();
		hunyiseHu = taishu.isHunyiseHu();
		duiduiHu = taishu.isDuiduiHu();
		sifengqiHu = taishu.isSifengqiHu();
		sancaishenHu = taishu.isSancaishenHu();
		tianHu = taishu.isTianHu();
		diHu = taishu.isDiHu();
		qingyiseHu = taishu.isQingyiseHu();
		shuangCaishengHu = taishu.isShuangCaishengHu();
		value = taishu.getValue();
	}

	public int getValue() {
		return value;
	}

	public boolean isHongzhongPeng() {
		return hongzhongPeng;
	}

	public boolean isHongzhongAnke() {
		return hongzhongAnke;
	}

	public boolean isHongzhongGang() {
		return hongzhongGang;
	}

	public boolean isFacaiPeng() {
		return facaiPeng;
	}

	public boolean isFacaiAnke() {
		return facaiAnke;
	}

	public boolean isFacaiGang() {
		return facaiGang;
	}

	public boolean isZuofengPeng() {
		return zuofengPeng;
	}

	public boolean isZuofengAnke() {
		return zuofengAnke;
	}

	public boolean isZuofengGang() {
		return zuofengGang;
	}

	public int getBaibanShu() {
		return baibanShu;
	}

	public boolean isDanzhangdiaoHu() {
		return danzhangdiaoHu;
	}

	public boolean isPingHu() {
		return pingHu;
	}

	public boolean isQianggangHu() {
		return qianggangHu;
	}

	public boolean isGangkaiHu() {
		return gangkaiHu;
	}

	public boolean isHunyiseHu() {
		return hunyiseHu;
	}

	public boolean isDuiduiHu() {
		return duiduiHu;
	}

	public boolean isSifengqiHu() {
		return sifengqiHu;
	}

	public boolean isSancaishenHu() {
		return sancaishenHu;
	}

	public boolean isTianHu() {
		return tianHu;
	}

	public boolean isDiHu() {
		return diHu;
	}

	public boolean isQingyiseHu() {
		return qingyiseHu;
	}

	public boolean isShuangCaishengHu() {
		return shuangCaishengHu;
	}

}
