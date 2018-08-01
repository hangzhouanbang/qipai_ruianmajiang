package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.action.MajiangPengAction;
import com.dml.majiang.action.MajiangPlayerPengActionUpdater;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {// TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

		// 碰的那个人要打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

	}

}
