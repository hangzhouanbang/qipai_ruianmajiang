package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangPengAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerPengActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {// TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

		// 碰的那个人要打出牌
		player.generateDaActions();

	}

}
