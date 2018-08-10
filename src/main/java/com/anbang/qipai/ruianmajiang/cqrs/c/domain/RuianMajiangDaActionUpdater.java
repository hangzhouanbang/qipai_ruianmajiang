package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

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
					RuianMajiangPanResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangPanResultBuilder) ju
							.getCurrentPanResultBuilder();
					int dihu = ruianMajiangJuResultBuilder.getDihu();
					boolean dapao = ruianMajiangJuResultBuilder.isDapao();
					GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
					// 先把这张牌放入计算器
					xiajiaPlayer.getShoupaiCalculator().addPai(daAction.getPai());
					RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestDianpaoHu(dapao, dihu,
							gouXingPanHu, xiajiaPlayer, baibanIsGuipai, daAction.getPai());
					// 再把这张牌拿出计算器
					xiajiaPlayer.getShoupaiCalculator().removePai(daAction.getPai());
					if (bestHu != null) {
						bestHu.setZimo(false);
						bestHu.setDianpaoPlayerId(player.getId());
						xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
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
