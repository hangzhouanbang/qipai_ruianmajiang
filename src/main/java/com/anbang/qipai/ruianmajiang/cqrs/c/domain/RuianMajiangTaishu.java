package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.serializer.ByteBufferAble;

public class RuianMajiangTaishu implements ByteBufferAble {
	/**
	 * 封顶台数。0为不封顶
	 */
	private int maxtai;
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

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		bb.putInt(maxtai);
		if (hongzhongPeng) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (hongzhongAnke) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (hongzhongGang) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (facaiPeng) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (facaiAnke) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (facaiGang) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (zuofengPeng) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (zuofengAnke) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (zuofengGang) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		bb.putInt(baibanShu);
		if (danzhangdiaoHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (pingHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (qianggangHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (gangkaiHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (hunyiseHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (duiduiHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (sifengqiHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (sancaishenHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (tianHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (diHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (qingyiseHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		if (shuangCaishengHu) {
			bb.putInt(1);
		} else {
			bb.putInt(0);
		}
		bb.putInt(value);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		maxtai = bb.getInt();
		if (bb.getInt() == 1) {
			hongzhongPeng = true;
		} else {
			hongzhongPeng = false;
		}
		if (bb.getInt() == 1) {
			hongzhongAnke = true;
		} else {
			hongzhongAnke = false;
		}
		if (bb.getInt() == 1) {
			hongzhongGang = true;
		} else {
			hongzhongGang = false;
		}
		if (bb.getInt() == 1) {
			facaiPeng = true;
		} else {
			facaiPeng = false;
		}
		if (bb.getInt() == 1) {
			facaiAnke = true;
		} else {
			facaiAnke = false;
		}
		if (bb.getInt() == 1) {
			facaiGang = true;
		} else {
			facaiGang = false;
		}
		if (bb.getInt() == 1) {
			zuofengPeng = true;
		} else {
			zuofengPeng = false;
		}
		if (bb.getInt() == 1) {
			zuofengAnke = true;
		} else {
			zuofengAnke = false;
		}
		if (bb.getInt() == 1) {
			zuofengGang = true;
		} else {
			zuofengGang = false;
		}
		bb.putInt(baibanShu);
		if (bb.getInt() == 1) {
			danzhangdiaoHu = true;
		} else {
			danzhangdiaoHu = false;
		}
		if (bb.getInt() == 1) {
			pingHu = true;
		} else {
			pingHu = false;
		}
		if (bb.getInt() == 1) {
			qianggangHu = true;
		} else {
			qianggangHu = false;
		}
		if (bb.getInt() == 1) {
			gangkaiHu = true;
		} else {
			gangkaiHu = false;
		}
		if (bb.getInt() == 1) {
			hunyiseHu = true;
		} else {
			hunyiseHu = false;
		}
		if (bb.getInt() == 1) {
			duiduiHu = true;
		} else {
			duiduiHu = false;
		}
		if (bb.getInt() == 1) {
			sifengqiHu = true;
		} else {
			sifengqiHu = false;
		}
		if (bb.getInt() == 1) {
			sancaishenHu = true;
		} else {
			sancaishenHu = false;
		}
		if (bb.getInt() == 1) {
			tianHu = true;
		} else {
			tianHu = false;
		}
		if (bb.getInt() == 1) {
			diHu = true;
		} else {
			diHu = false;
		}
		if (bb.getInt() == 1) {
			qingyiseHu = true;
		} else {
			qingyiseHu = false;
		}
		if (bb.getInt() == 1) {
			shuangCaishengHu = true;
		} else {
			shuangCaishengHu = false;
		}
		bb.putInt(value);
	}

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

	public int getMaxtai() {
		return maxtai;
	}

	public void setMaxtai(int maxtai) {
		this.maxtai = maxtai;
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
