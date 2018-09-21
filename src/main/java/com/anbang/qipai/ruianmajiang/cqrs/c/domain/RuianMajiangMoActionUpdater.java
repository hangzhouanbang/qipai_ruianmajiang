package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionUpdater;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class RuianMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		int liupai = 14;
		RuianMajiangChiPengGangActionStatisticsListener gangCounter = ju.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		if (gangCounter.getCount() > 0) {
			liupai += (4 + (gangCounter.getCount() - 1) * 2);
		}
		Pan currentPan = ju.getCurrentPan();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		Set<MajiangPai> guipaiTypeSet = player.getGuipaiTypeSet();
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		player.clearActionCandidates();
		int playersCount = currentPan.countPlayers();
		int avaliablePaiLeft = currentPan.countAvaliablePai();
		if (avaliablePaiLeft - liupai == 0) {// 没牌了
			// 当然啥也不干了
		} else {
			// 摸到公开牌了要补牌(继续摸牌)
			if (baibanIsGuipai) {// 白板是鬼牌
				if (gangmoShoupai.equals(MajiangPai.hongzhong)) {// 红中是公开牌
					player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
					return;
				}
			} else {// 白板不是鬼牌
				if (gangmoShoupai.equals(MajiangPai.baiban)) { // 白板是公开牌
					player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
					return;
				}
			}

			// 有手牌或刻子可以杠这个摸来的牌
			player.tryShoupaigangmoAndGenerateCandidateAction();
			player.tryKezigangmoAndGenerateCandidateAction();

			// 杠四个手牌
			player.tryGangsigeshoupaiAndGenerateCandidateAction();

			// 刻子杠手牌
			player.tryKezigangshoupaiAndGenerateCandidateAction();

			// 胡
			RuianMajiangPanResultBuilder ruianMajiangPanResultBuilder = (RuianMajiangPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			int dihu = ruianMajiangPanResultBuilder.getDihu();
			boolean dapao = ruianMajiangPanResultBuilder.isDapao();
			GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();

			boolean couldTianhu = false;
			if (currentPan.getZhuangPlayerId().equals(player.getId())) {
				if (player.countAllFangruShoupai() == 0) {
					couldTianhu = true;
				}
			}

			RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestZimoHu(couldTianhu, dapao, dihu,
					gouXingPanHu, player, moAction, baibanIsGuipai);
			if (bestHu != null) {
				bestHu.setZimo(true);
				player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
			} else {
				// 非胡牌型特殊胡-三财神
				int guipaiCount = player.countGuipai();
				if (guipaiCount == 3) {
					RuianMajiangPanPlayerScore score = RuianMajiangJiesuanCalculator
							.calculateBestScoreForBuhuPlayer(dapao, dihu, player, baibanIsGuipai);
					RuianMajiangHu sancaishenHu = new RuianMajiangHu(score);
					player.addActionCandidate(new MajiangHuAction(player.getId(), sancaishenHu));
				}
			}

			if (guipaiTypeSet.contains(gangmoShoupai) && fangruShoupaiList.size() == 0) {
				// 当手上没有除鬼牌之外的牌时不能过
			} else {
				// 需要有“过”
				player.checkAndGenerateGuoCandidateAction();
			}

			if ((avaliablePaiLeft - liupai) < playersCount) {// 进入流局前最后4张
				// 啥也不能干，下家摸牌
				if (player.getActionCandidates().isEmpty()) {
					MajiangPlayer xiajia = currentPan.findXiajia(player);
					xiajia.addActionCandidate(new MajiangMoAction(xiajia.getId(), new LundaoMopai()));
				}
			} else {
				// 啥也不能干，那只能打出牌
				RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
						.getActionStatisticsListenerManager()
						.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
				for (MajiangPai pai : fangruShoupaiList) {
					if (MajiangPai.isZipai(pai) && juezhangStatisticsListener.ifJuezhang(pai)) {
						player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
					}
				}
				if (MajiangPai.isZipai(gangmoShoupai) && juezhangStatisticsListener.ifJuezhang(gangmoShoupai)) {
					player.addActionCandidate(new MajiangDaAction(player.getId(), gangmoShoupai));
				}
				if (player.getActionCandidates().isEmpty()) {
					player.generateDaActions();
				}
			}

		}

	}

}
