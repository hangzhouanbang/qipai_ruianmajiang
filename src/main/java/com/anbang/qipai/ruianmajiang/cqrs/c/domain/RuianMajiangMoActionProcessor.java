package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPlayerMoActionProcessor;
import com.dml.majiang.Pan;

public class RuianMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();

		// TODO 摸牌理由如果是补牌，那需要把原来的未处理牌先放入公开牌

		currentPan.playerMoPai(action.getActionPlayerId());
		currentPan.setActivePaiCursor(null);
	}

}
