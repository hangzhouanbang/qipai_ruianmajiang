package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.action.LundaoMopai;
import com.dml.majiang.action.MajiangDaAction;
import com.dml.majiang.action.MajiangGuoAction;
import com.dml.majiang.action.MajiangMoAction;
import com.dml.majiang.action.MajiangPlayerAction;
import com.dml.majiang.action.MajiangPlayerActionType;
import com.dml.majiang.action.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;

public class RuianMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());
		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠
			// 那要我打牌
			player.generateDaActions();
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
				// 打牌那家的下家摸牌
				MajiangDaAction daAction = (MajiangDaAction) action;
				MajiangPlayer xiajiaPlayer = currentPan
						.findXiajia(currentPan.findPlayerById(daAction.getActionPlayerId()));
				xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
			}
		} else {
		}
	}

}
