package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.gang.GangCounter;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;

public class RuianMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) throws Exception {

		int liupai = 14;
		GangCounter gangCounter = ju.getActionStatisticsListenerManager().findListener(GangCounter.class);
		if (gangCounter.getCount() > 0) {
			liupai += (4 + (gangCounter.getCount() - 1) * 2);
		}
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());

		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());
		int playersCount = currentPan.countPlayers();
		int avaliablePaiLeft = currentPan.countAvaliablePai();

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠

			if ((avaliablePaiLeft - liupai) < playersCount) {// 进入流局前最后4张
				// 打牌那家的下家摸牌
				MajiangPlayer xiajiaPlayer = currentPan
						.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
				xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
			} else {
				// 那要我打牌
				player.generateDaActions();
			}
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates()) {// 如果所有玩家啥也干不了
				// 打牌那家的下家摸牌
				MajiangPlayer xiajiaPlayer = currentPan
						.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
				xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
			}
		} else {
		}
	}

}
