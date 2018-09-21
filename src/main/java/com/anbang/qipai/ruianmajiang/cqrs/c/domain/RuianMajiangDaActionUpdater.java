package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.DianpaoDihuOpportunityDetector;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class RuianMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) throws Exception {

		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
		// 是否是地胡
		DianpaoDihuOpportunityDetector dianpaoDihuOpportunityDetector = ju.getActionStatisticsListenerManager()
				.findListener(DianpaoDihuOpportunityDetector.class);
		boolean couldDihu = dianpaoDihuOpportunityDetector.ifDihuOpportunity();
		daPlayer.clearActionCandidates();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		MajiangPlayer xiajiaPlayer = currentPan.findXiajia(daPlayer);
		xiajiaPlayer.clearActionCandidates();
		// 下家可以吃
		List<MajiangPai> fangruShoupaiList = xiajiaPlayer.getFangruShoupaiList();
		if (fangruShoupaiList.size() != 2) {
			xiajiaPlayer.tryChiAndGenerateCandidateActions(daAction.getActionPlayerId(), daAction.getPai());
		}
		while (true) {
			if (!xiajiaPlayer.getId().equals(daAction.getActionPlayerId())) {
				// 其他的可以碰杠胡
				List<MajiangPai> fangruShoupaiList1 = xiajiaPlayer.getFangruShoupaiList();
				if (fangruShoupaiList1.size() != 2) {
					xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				}
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				// 点炮胡
				RuianMajiangPanResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangPanResultBuilder) ju
						.getCurrentPanResultBuilder();
				int dihu = ruianMajiangJuResultBuilder.getDihu();
				boolean dapao = ruianMajiangJuResultBuilder.isDapao();
				GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
				// 先把这张牌放入计算器
				xiajiaPlayer.getShoupaiCalculator().addPai(daAction.getPai());
				RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestDianpaoHu(couldDihu, dapao, dihu,
						gouXingPanHu, xiajiaPlayer, baibanIsGuipai, daAction.getPai());
				// 再把这张牌拿出计算器
				xiajiaPlayer.getShoupaiCalculator().removePai(daAction.getPai());
				if (bestHu != null) {
					bestHu.setZimo(false);
					bestHu.setDianpao(true);
					bestHu.setDianpaoPlayerId(daPlayer.getId());
					xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
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
			xiajiaPlayer = currentPan.findXiajia(daPlayer);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}

		// TODO 接着做

	}

}
