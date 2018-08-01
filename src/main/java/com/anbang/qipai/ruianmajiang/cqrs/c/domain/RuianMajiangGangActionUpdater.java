package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.action.MajiangGangAction;
import com.dml.majiang.action.MajiangMoAction;
import com.dml.majiang.action.MajiangPlayerGangActionUpdater;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.GanghouBupai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;

public class RuianMajiangGangActionUpdater implements MajiangPlayerGangActionUpdater {// TODO 改成通用GangActionUpdater

	@Override
	public void updateActions(MajiangGangAction gangAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// 杠完之后要摸牌
		if (player.getActionCandidates().isEmpty()) {
			player.addActionCandidate(new MajiangMoAction(player.getId(),
					new GanghouBupai(gangAction.getPai(), gangAction.getGangType())));
		}
	}

}
