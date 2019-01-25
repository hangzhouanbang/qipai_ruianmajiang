package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.SiFengQiMoDaActionListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.da.MajiangPlayerDaActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.listener.comprehensive.DianpaoDihuOpportunityDetector;
import com.dml.majiang.player.action.listener.comprehensive.GuoPengBuPengStatisticsListener;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class RuianMajiangDaActionUpdater implements MajiangPlayerDaActionUpdater {

	@Override
	public void updateActions(MajiangDaAction daAction, Ju ju) {

		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer daPlayer = currentPan.findPlayerById(daAction.getActionPlayerId());
		List<MajiangPai> daplayerFangruShoupaiList = daPlayer.getFangruShoupaiList();
		// 是否是地胡
		DianpaoDihuOpportunityDetector dianpaoDihuOpportunityDetector = ju.getActionStatisticsListenerManager()
				.findListener(DianpaoDihuOpportunityDetector.class);
		boolean couldDihu = dianpaoDihuOpportunityDetector.ifDihuOpportunity();
		daPlayer.clearActionCandidates();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		GuoPengBuPengStatisticsListener guoPengBuPengStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(GuoPengBuPengStatisticsListener.class);
		Map<String, List<MajiangPai>> canNotPengPlayersPaiMap = guoPengBuPengStatisticsListener
				.getCanNotPengPlayersPaiMap();

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
					boolean canPeng = true;// 可以碰
					if (canNotPengPlayersPaiMap.containsKey(xiajiaPlayer.getId())) {
						List<MajiangPai> canNotPengPaiList = canNotPengPlayersPaiMap.get(xiajiaPlayer.getId());
						if (canNotPengPaiList != null && !canNotPengPaiList.isEmpty()) {
							for (MajiangPai pai : canNotPengPaiList) {
								if (pai.equals(daAction.getPai())) {
									canPeng = false;
									break;
								}
							}
						}
					}
					if (canPeng) {
						xiajiaPlayer.tryPengAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
					}
				}
				xiajiaPlayer.tryGangdachuAndGenerateCandidateAction(daAction.getActionPlayerId(), daAction.getPai());
				// 点炮胡
				RuianMajiangPanResultBuilder ruianMajiangPanResultBuilder = (RuianMajiangPanResultBuilder) ju
						.getCurrentPanResultBuilder();
				int dihu = ruianMajiangPanResultBuilder.getDihu();
				boolean dapao = ruianMajiangPanResultBuilder.isDapao();
				int maxtai = ruianMajiangPanResultBuilder.getMaxtai();
				GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
				// 先把这张牌放入计算器
				xiajiaPlayer.getShoupaiCalculator().addPai(daAction.getPai());
				final SiFengQiMoDaActionListener siFengQiMoDaActionListener = ju.getActionStatisticsListenerManager()
						.findListener(SiFengQiMoDaActionListener.class);
				siFengQiMoDaActionListener.put(xiajiaPlayer.getId(), daAction.getPai());
				final boolean couldSiFengQi = siFengQiMoDaActionListener.couldSiFengQi(xiajiaPlayer.getId());
				RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestDianpaoHu(couldSiFengQi,
						couldDihu && !currentPan.getZhuangPlayerId().equals(xiajiaPlayer.getId()), dapao, dihu, maxtai,
						gouXingPanHu, xiajiaPlayer, baibanIsGuipai, daAction.getPai());
				// 再把这张牌拿出计算器
				xiajiaPlayer.getShoupaiCalculator().removePai(daAction.getPai());
				if (bestHu != null) {
					bestHu.setZimo(false);
					bestHu.setDianpao(true);
					bestHu.setDianpaoPlayerId(daPlayer.getId());
					xiajiaPlayer.addActionCandidate(new MajiangHuAction(xiajiaPlayer.getId(), bestHu));
				} else {
					siFengQiMoDaActionListener.remove(xiajiaPlayer.getId(), daAction.getPai());
				}

				xiajiaPlayer.checkAndGenerateGuoCandidateAction();
			} else {
				break;
			}
			xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
			xiajiaPlayer.clearActionCandidates();
		}
		if (daplayerFangruShoupaiList.size() == 0) {// 如果手牌只有财神时需要有胡和过，胡自己打出去的那张牌
			// 胡
			RuianMajiangPanResultBuilder ruianMajiangPanResultBuilder = (RuianMajiangPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			int dihu = ruianMajiangPanResultBuilder.getDihu();
			boolean dapao = ruianMajiangPanResultBuilder.isDapao();
			int maxtai = ruianMajiangPanResultBuilder.getMaxtai();
			GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
			daPlayer.setGangmoShoupai(daAction.getPai());
			final SiFengQiMoDaActionListener siFengQiMoDaActionListener = ju.getActionStatisticsListenerManager()
					.findListener(SiFengQiMoDaActionListener.class);
			final boolean couldSiFengQi = siFengQiMoDaActionListener.couldSiFengQi(daAction.getActionPlayerId());
			RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestZimoHu(couldSiFengQi, false, dapao, dihu,
					maxtai, gouXingPanHu, daPlayer, new MajiangMoAction(daPlayer.getId(), new LundaoMopai()),
					baibanIsGuipai);
			daPlayer.setGangmoShoupai(null);
			if (bestHu != null) {
				bestHu.setZimo(true);// 全求神算自摸
				bestHu.setDianpaoPlayerId(daPlayer.getId());
				daPlayer.addActionCandidate(new MajiangHuAction(daPlayer.getId(), bestHu));
			}
			daPlayer.checkAndGenerateGuoCandidateAction();
		}
		currentPan.disablePlayerActionsByHuPengGangChiPriority();// 吃碰杠胡优先级判断
		// 如果所有玩家啥也做不了,那就下家摸牌
		if (currentPan.allPlayerHasNoActionCandidates()) {
			xiajiaPlayer = currentPan.findXiajia(daPlayer);
			xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
		}

		// TODO 接着做

	}

}
