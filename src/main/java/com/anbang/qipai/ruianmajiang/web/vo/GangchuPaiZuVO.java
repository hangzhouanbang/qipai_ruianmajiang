package com.anbang.qipai.ruianmajiang.web.vo;

import com.dml.majiang.GangType;
import com.dml.majiang.GangchuPaiZu;
import com.dml.majiang.MajiangPai;

public class GangchuPaiZuVO {

	private MajiangPai paiType;
	private GangType gangType;

	public GangchuPaiZuVO(GangchuPaiZu gangchuPaiZu) {
		paiType = gangchuPaiZu.getGangzi().getPaiType();
		gangType = gangchuPaiZu.getGangType();
	}

	public MajiangPai getPaiType() {
		return paiType;
	}

	public GangType getGangType() {
		return gangType;
	}

}
