package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

public class RuianMajiangHushu {
	private int dihu;
	private boolean hu;
	private boolean zimoHu;
	private boolean biandangHu;
	private boolean qiandangHu;
	private boolean dandiaoHu;
	private int yijiupengShu;
	private int erbapengShu;
	private int fengzipengShu;
	private int yijiuankeShu;
	private int erbaankeShu;
	private int fengziankeShu;
	private int yijiuminggangShu;
	private int erbaminggangShu;
	private int fengziminggangShu;
	private int yijiuangangShu;
	private int erbaangangShu;
	private int fengziangangShu;
	private boolean hongzhongDuizi;
	private boolean facaiDuizi;
	private boolean zuofengDuizi;
	private int baibanShu;
	private RuianMajiangTaishu taishu;
	private int value;

	public void calculate() {
		taishu.calculate();
		int hu = 0;
		if (zimoHu) {
			hu += 2;
		}
		if (biandangHu) {
			hu += 2;
		}
		if (qiandangHu) {
			hu += 2;
		}
		if (dandiaoHu) {
			hu += 2;
		}
		hu += (yijiupengShu * 4);
		hu += (erbapengShu * 2);
		hu += (fengzipengShu * 4);
		hu += (yijiuankeShu * 8);
		hu += (erbaankeShu * 4);
		hu += (fengziankeShu * 8);
		hu += (yijiuminggangShu * 16);
		hu += (erbaminggangShu * 8);
		hu += (fengziminggangShu * 16);
		hu += (yijiuangangShu * 32);
		hu += (erbaangangShu * 16);
		hu += (fengziangangShu * 32);
		if (hongzhongDuizi) {
			hu += 2;
		}
		if (facaiDuizi) {
			hu += 2;
		}
		if (zuofengDuizi) {
			hu += 2;
		}
		hu += (baibanShu * 4);
		if (this.hu) {
			if (taishu.getValue() < 3) {
				value = (int) ((dihu + hu) * Math.pow(2, taishu.getValue()));
			} else {
				value = (500 + 200 * (taishu.getValue() - 3));
			}
		} else {
			value = (int) (hu * Math.pow(2, taishu.getValue()));
			if (value > 500) {
				value = 500;
			}
		}
	}

	public int quzhengValue() {
		return quzheng(value);
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

	public int getDihu() {
		return dihu;
	}

	public void setDihu(int dihu) {
		this.dihu = dihu;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZimoHu() {
		return zimoHu;
	}

	public void setZimoHu(boolean zimoHu) {
		this.zimoHu = zimoHu;
	}

	public boolean isBiandangHu() {
		return biandangHu;
	}

	public void setBiandangHu(boolean biandangHu) {
		this.biandangHu = biandangHu;
	}

	public boolean isQiandangHu() {
		return qiandangHu;
	}

	public void setQiandangHu(boolean qiandangHu) {
		this.qiandangHu = qiandangHu;
	}

	public boolean isDandiaoHu() {
		return dandiaoHu;
	}

	public void setDandiaoHu(boolean dandiaoHu) {
		this.dandiaoHu = dandiaoHu;
	}

	public int getYijiupengShu() {
		return yijiupengShu;
	}

	public void setYijiupengShu(int yijiupengShu) {
		this.yijiupengShu = yijiupengShu;
	}

	public int getErbapengShu() {
		return erbapengShu;
	}

	public void setErbapengShu(int erbapengShu) {
		this.erbapengShu = erbapengShu;
	}

	public int getFengzipengShu() {
		return fengzipengShu;
	}

	public void setFengzipengShu(int fengzipengShu) {
		this.fengzipengShu = fengzipengShu;
	}

	public int getYijiuankeShu() {
		return yijiuankeShu;
	}

	public void setYijiuankeShu(int yijiuankeShu) {
		this.yijiuankeShu = yijiuankeShu;
	}

	public int getErbaankeShu() {
		return erbaankeShu;
	}

	public void setErbaankeShu(int erbaankeShu) {
		this.erbaankeShu = erbaankeShu;
	}

	public int getFengziankeShu() {
		return fengziankeShu;
	}

	public void setFengziankeShu(int fengziankeShu) {
		this.fengziankeShu = fengziankeShu;
	}

	public int getYijiuminggangShu() {
		return yijiuminggangShu;
	}

	public void setYijiuminggangShu(int yijiuminggangShu) {
		this.yijiuminggangShu = yijiuminggangShu;
	}

	public int getErbaminggangShu() {
		return erbaminggangShu;
	}

	public void setErbaminggangShu(int erbaminggangShu) {
		this.erbaminggangShu = erbaminggangShu;
	}

	public int getFengziminggangShu() {
		return fengziminggangShu;
	}

	public void setFengziminggangShu(int fengziminggangShu) {
		this.fengziminggangShu = fengziminggangShu;
	}

	public int getYijiuangangShu() {
		return yijiuangangShu;
	}

	public void setYijiuangangShu(int yijiuangangShu) {
		this.yijiuangangShu = yijiuangangShu;
	}

	public int getErbaangangShu() {
		return erbaangangShu;
	}

	public void setErbaangangShu(int erbaangangShu) {
		this.erbaangangShu = erbaangangShu;
	}

	public int getFengziangangShu() {
		return fengziangangShu;
	}

	public void setFengziangangShu(int fengziangangShu) {
		this.fengziangangShu = fengziangangShu;
	}

	public boolean isHongzhongDuizi() {
		return hongzhongDuizi;
	}

	public void setHongzhongDuizi(boolean hongzhongDuizi) {
		this.hongzhongDuizi = hongzhongDuizi;
	}

	public boolean isFacaiDuizi() {
		return facaiDuizi;
	}

	public void setFacaiDuizi(boolean facaiDuizi) {
		this.facaiDuizi = facaiDuizi;
	}

	public boolean isZuofengDuizi() {
		return zuofengDuizi;
	}

	public void setZuofengDuizi(boolean zuofengDuizi) {
		this.zuofengDuizi = zuofengDuizi;
	}

	public int getBaibanShu() {
		return baibanShu;
	}

	public void setBaibanShu(int baibanShu) {
		this.baibanShu = baibanShu;
	}

	public RuianMajiangTaishu getTaishu() {
		return taishu;
	}

	public void setTaishu(RuianMajiangTaishu taishu) {
		this.taishu = taishu;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
