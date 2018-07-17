package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.LundaoMopai;
import com.dml.majiang.MajiangDaAction;
import com.dml.majiang.MajiangMoAction;
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
		xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getActionPlayerId(), daAction.getPai());
		xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
		xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
		xiajiaPlayer.checkAndGenerateGuoCandidateAction();

		while (true) {
			xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
			if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
				xiajiaPlayer.clearActionCandidates();
				// 其他的可以碰杠
				xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				xiajiaPlayer.checkAndGenerateGuoCandidateAction();
			} else {
				break;
			}
		}

		// 如果所有玩家啥也做不了,那就下家摸牌
		if (currentPan.allPlayerHasNoActionCandidates()) {
			xiajiaPlayer = currentPan.findXiajia(player);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}

		// TODO 接着做

	}

}
