package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.Ju;
import com.dml.majiang.LundaoMopai;
import com.dml.majiang.MajiangDaAction;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerDaActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) throws Exception {

		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(daAction.getActionPlayerId());
		player.clearActionCandidates();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		MajiangPlayer xiajiaPlayer = currentPan.findXiajia(player);
		xiajiaPlayer.clearActionCandidates();
		// 下家可以吃
		xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getActionPlayerId(), daAction.getPai());

		boolean anyPlayerHu = false;
		while (true) {
			if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
				// 其他的可以碰杠胡
				xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());

				if (!anyPlayerHu) {
					// 点炮胡
					RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangJuResultBuilder) ju
							.getJuResultBuilder();
					int dihu = ruianMajiangJuResultBuilder.getDihu();
					GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
					RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestDianpaoHu(dihu, gouXingPanHu,
							player, baibanIsGuipai, daAction.getPai());
					if (bestHu != null) {
						player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
						anyPlayerHu = true;
					} else {
						// // 非胡牌型特殊胡-三财神
						// MoGuipaiCounter moGuipaiCounter =
						// ju.getActionStatisticsListenerManager().findListener(MoGuipaiCounter.class);
						// if (moGuipaiCounter.getCount() == 3) {
						//
						// }
					}
				}

				xiajiaPlayer.checkAndGenerateGuoCandidateAction();
			} else {
				break;
			}
			xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
			xiajiaPlayer.clearActionCandidates();
		}

		// 如果所有玩家啥也做不了,那就下家摸牌
		if (currentPan.allPlayerHasNoActionCandidates()) {
			xiajiaPlayer = currentPan.findXiajia(player);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}

		// TODO 接着做

	}

}
