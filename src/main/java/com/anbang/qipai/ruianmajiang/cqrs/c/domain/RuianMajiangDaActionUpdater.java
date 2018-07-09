package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangDaAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerDaActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) throws Exception {

		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(daAction.getActionPlayerId());
		player.clearActionCandidates();

		MajiangPlayer xiajiaPlayer = currentPan.findXiajia(player);
		xiajiaPlayer.clearActionCandidates();
		// 下家可以吃碰杠
		xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getPai());

		// TODO 接着做

	}

}
