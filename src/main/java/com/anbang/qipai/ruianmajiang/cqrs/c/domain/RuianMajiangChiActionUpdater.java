package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangChiAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerChiActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater { // TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();

		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());

		// 吃的那个人要打出牌
		player.generateDaActions();

		// TODO 接着做
	}

}
