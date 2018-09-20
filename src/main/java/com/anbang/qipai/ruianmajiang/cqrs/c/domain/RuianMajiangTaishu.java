package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangTaishu {
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

	public void calculate() {
		int tai = 0;
		if (hongzhongPeng) {
			tai++;
		}
		if (hongzhongAnke) {
			tai++;
		}
		if (hongzhongGang) {
			tai++;
		}
		if (facaiPeng) {
			tai++;
		}
		if (facaiAnke) {
			tai++;
		}
		if (facaiGang) {
			tai++;
		}
		if (zuofengPeng) {
			tai++;
		}
		if (zuofengAnke) {
			tai++;
		}
		if (zuofengGang) {
			tai++;
		}
		tai += baibanShu;
		if (danzhangdiaoHu) {
			tai += 3;
		}
		if (pingHu) {
			tai++;
		}
		if (qianggangHu) {
			tai++;
		}
		if (gangkaiHu) {
			tai++;
		}
		if (hunyiseHu) {
			tai += 2;
		}
		if (duiduiHu) {
			tai += 2;
		}
		if (sifengqiHu) {
			if (zuofengPeng || zuofengAnke || zuofengGang) {
				tai += 2;
			} else {
				tai += 3;
			}
		}
		if (sancaishenHu) {
			tai += 3;
		}
		if (tianHu) {
			tai += 3;
		}
		if (diHu) {
			tai += 3;
		}
		if (qingyiseHu) {
			tai += 3;
		}
		if (shuangCaishengHu) {
			tai++;
		}
		value = tai;
	}

	public boolean isHongzhongPeng() {
		return hongzhongPeng;
	}

	public void setHongzhongPeng(boolean hongzhongPeng) {
		this.hongzhongPeng = hongzhongPeng;
	}

	public boolean isHongzhongAnke() {
		return hongzhongAnke;
	}

	public void setHongzhongAnke(boolean hongzhongAnke) {
		this.hongzhongAnke = hongzhongAnke;
	}

	public boolean isHongzhongGang() {
		return hongzhongGang;
	}

	public void setHongzhongGang(boolean hongzhongGang) {
		this.hongzhongGang = hongzhongGang;
	}

	public boolean isFacaiPeng() {
		return facaiPeng;
	}

	public void setFacaiPeng(boolean facaiPeng) {
		this.facaiPeng = facaiPeng;
	}

	public boolean isFacaiAnke() {
		return facaiAnke;
	}

	public void setFacaiAnke(boolean facaiAnke) {
		this.facaiAnke = facaiAnke;
	}

	public boolean isFacaiGang() {
		return facaiGang;
	}

	public void setFacaiGang(boolean facaiGang) {
		this.facaiGang = facaiGang;
	}

	public boolean isZuofengPeng() {
		return zuofengPeng;
	}

	public void setZuofengPeng(boolean zuofengPeng) {
		this.zuofengPeng = zuofengPeng;
	}

	public boolean isZuofengAnke() {
		return zuofengAnke;
	}

	public void setZuofengAnke(boolean zuofengAnke) {
		this.zuofengAnke = zuofengAnke;
	}

	public boolean isZuofengGang() {
		return zuofengGang;
	}

	public void setZuofengGang(boolean zuofengGang) {
		this.zuofengGang = zuofengGang;
	}

	public int getBaibanShu() {
		return baibanShu;
	}

	public void setBaibanShu(int baibanShu) {
		this.baibanShu = baibanShu;
	}

	public boolean isDanzhangdiaoHu() {
		return danzhangdiaoHu;
	}

	public void setDanzhangdiaoHu(boolean danzhangdiaoHu) {
		this.danzhangdiaoHu = danzhangdiaoHu;
	}

	public boolean isPingHu() {
		return pingHu;
	}

	public void setPingHu(boolean pingHu) {
		this.pingHu = pingHu;
	}

	public boolean isQianggangHu() {
		return qianggangHu;
	}

	public void setQianggangHu(boolean qianggangHu) {
		this.qianggangHu = qianggangHu;
	}

	public boolean isGangkaiHu() {
		return gangkaiHu;
	}

	public void setGangkaiHu(boolean gangkaiHu) {
		this.gangkaiHu = gangkaiHu;
	}

	public boolean isHunyiseHu() {
		return hunyiseHu;
	}

	public void setHunyiseHu(boolean hunyiseHu) {
		this.hunyiseHu = hunyiseHu;
	}

	public boolean isDuiduiHu() {
		return duiduiHu;
	}

	public void setDuiduiHu(boolean duiduiHu) {
		this.duiduiHu = duiduiHu;
	}

	public boolean isSifengqiHu() {
		return sifengqiHu;
	}

	public void setSifengqiHu(boolean sifengqiHu) {
		this.sifengqiHu = sifengqiHu;
	}

	public boolean isSancaishenHu() {
		return sancaishenHu;
	}

	public void setSancaishenHu(boolean sancaishenHu) {
		this.sancaishenHu = sancaishenHu;
	}

	public boolean isTianHu() {
		return tianHu;
	}

	public void setTianHu(boolean tianHu) {
		this.tianHu = tianHu;
	}

	public boolean isDiHu() {
		return diHu;
	}

	public void setDiHu(boolean diHu) {
		this.diHu = diHu;
	}

	public boolean isQingyiseHu() {
		return qingyiseHu;
	}

	public void setQingyiseHu(boolean qingyiseHu) {
		this.qingyiseHu = qingyiseHu;
	}

	public boolean isShuangCaishengHu() {
		return shuangCaishengHu;
	}

	public void setShuangCaishengHu(boolean shuangCaishengHu) {
		this.shuangCaishengHu = shuangCaishengHu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
