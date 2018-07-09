package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangChiAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerChiActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater {

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());
		player.clearActionCandidates();
		// TODO 接着做
	}

}
