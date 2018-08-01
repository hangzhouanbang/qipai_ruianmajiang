package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.hu.MajiangPlayerHuActionProcessor;

public class RuianMajiangHuActionProcessor implements MajiangPlayerHuActionProcessor {

	@Override
	public void process(MajiangHuAction action, Ju ju) throws Exception {
		RuianMajiangHu hu = (RuianMajiangHu) action.getHu();
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer huPlayer = currentPan.findPlayerById(action.getActionPlayerId());
		huPlayer.setHu(hu);
	}

}
