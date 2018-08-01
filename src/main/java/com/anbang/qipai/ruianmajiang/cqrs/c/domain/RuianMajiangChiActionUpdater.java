package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionUpdater;

public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater { // TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();

		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());

		// 吃的那个人要打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

		// TODO 接着做
	}

}
