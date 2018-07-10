package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangPengAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerPengActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		player.clearActionCandidates();
		// TODO 接着做
	}

}
