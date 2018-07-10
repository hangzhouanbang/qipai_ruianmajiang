package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangGuoAction;
import com.dml.majiang.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());
		// TODO 接着做
	}

}
