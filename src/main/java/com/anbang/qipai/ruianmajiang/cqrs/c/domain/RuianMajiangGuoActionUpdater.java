package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.LundaoMopai;
import com.dml.majiang.MajiangGuoAction;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());
		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());

		if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
			// 那就我摸牌
			player.addActionCandidate(new MajiangMoAction(player.getId(), new LundaoMopai()));
		}
	}

}
