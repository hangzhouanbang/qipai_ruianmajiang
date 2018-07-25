package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerHuActionProcessor;
import com.dml.majiang.Pan;

public class RuianMajiangHuActionProcessor implements MajiangPlayerHuActionProcessor {

	@Override
	public void process(MajiangHuAction action, Ju ju) throws Exception {
		RuianMajiangHu hu = (RuianMajiangHu) action.getHu();
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer huPlayer = currentPan.findPlayerById(action.getActionPlayerId());
		huPlayer.setHu(hu);
	}

}
