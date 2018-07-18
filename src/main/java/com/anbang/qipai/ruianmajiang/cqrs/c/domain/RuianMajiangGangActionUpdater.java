package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.GanghouBupai;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangGangAction;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerGangActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangGangActionUpdater implements MajiangPlayerGangActionUpdater {// TODO 改成通用GangActionUpdater

	@Override
	public void updateActions(MajiangGangAction gangAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());

		// 杠完之后要摸牌
		player.addActionCandidate(
				new MajiangMoAction(player.getId(), new GanghouBupai(gangAction.getPai(), gangAction.getGangType())));
	}

}
