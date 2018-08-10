package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangPao {
	private boolean hongzhongPeng;
	private boolean hongzhongAnke;
	private boolean hongzhongGang;
	private boolean facaiPeng;
	private boolean facaiAnke;
	private boolean facaiGang;
	private boolean zuofengPeng;
	private boolean zuofengAnke;
	private boolean zuofengGang;
	private boolean hu;
	private int baibanShu;
	private int caishenShu;
	private int value;

	public void calculate() {
		int pao = 0;
		if (hongzhongPeng) {
			pao++;
		}
		if (hongzhongAnke) {
			pao++;
		}
		if (hongzhongGang) {
			pao++;
		}
		if (facaiPeng) {
			pao++;
		}
		if (facaiAnke) {
			pao++;
		}
		if (facaiGang) {
			pao++;
		}
		if (zuofengPeng) {
			pao++;
		}
		if (zuofengAnke) {
			pao++;
		}
		if (zuofengGang) {
			pao++;
		}
		if (hu) {
			pao++;
		}

		boolean siBaiban = (baibanShu == 4);
		boolean sanCaishen = (caishenShu == 3);

		if (siBaiban && sanCaishen) {
			pao = (7 + pao) * 4;
		} else if (siBaiban) {
			pao += caishenShu;
			pao = (4 + pao) * 2;
		} else if (sanCaishen) {
			pao += baibanShu;
			pao = (3 + pao) * 2;
		} else {
			pao += caishenShu;
			pao += baibanShu;
		}
		value = pao;
	}

	public int jiesuan(RuianMajiangPao anotherPlayerPao) {
		return value - anotherPlayerPao.getValue();
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

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public int getBaibanShu() {
		return baibanShu;
	}

	public void setBaibanShu(int baibanShu) {
		this.baibanShu = baibanShu;
	}

	public int getCaishenShu() {
		return caishenShu;
	}

	public void setCaishenShu(int caishenShu) {
		this.caishenShu = caishenShu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
