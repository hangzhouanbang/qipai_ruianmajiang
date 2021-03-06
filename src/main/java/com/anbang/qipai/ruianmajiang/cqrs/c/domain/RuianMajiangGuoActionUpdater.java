package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.guo.MajiangGuoAction;
import com.dml.majiang.player.action.guo.MajiangPlayerGuoActionUpdater;
import com.dml.majiang.player.action.mo.LundaoMopai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;

public class RuianMajiangGuoActionUpdater implements MajiangPlayerGuoActionUpdater {

	@Override
	public void updateActions(MajiangGuoAction guoAction, Ju ju) {

		int liupai = 14;
		RuianMajiangChiPengGangActionStatisticsListener gangCounter = ju.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		if (gangCounter.getCount() > 0) {
			liupai += (4 + (gangCounter.getCount() - 1) * 2);
		}
		Pan currentPan = ju.getCurrentPan();
		currentPan.playerClearActionCandidates(guoAction.getActionPlayerId());

		MajiangPlayer player = currentPan.findPlayerById(guoAction.getActionPlayerId());
		int playersCount = currentPan.countPlayers();
		int avaliablePaiLeft = currentPan.countAvaliablePai();

		// 首先看一下,我过的是什么? 是我摸牌之后的胡,杠? 还是别人打出牌之后我可以吃碰杠胡
		PanActionFrame latestPanActionFrame = currentPan.findNotGuoLatestActionFrame();
		MajiangPlayerAction action = latestPanActionFrame.getAction();
		if (action.getType().equals(MajiangPlayerActionType.mo)) {// 过的是我摸牌之后的胡,杠

			if ((avaliablePaiLeft - liupai) < playersCount) {// 进入流局前最后4张
				// 打牌那家的下家摸牌
				MajiangPlayer xiajiaPlayer = currentPan
						.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
				xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
			} else {
				// 那要我打牌
				MajiangDaAction latestDaAction = (MajiangDaAction) currentPan.findLatestDaActionFrame();
				List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
				MajiangPai gangmoShoupai = player.getGangmoShoupai();
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
				} else {// 跟风
					for (MajiangPai pai : fangruShoupaiList) {
						if (latestDaAction != null && MajiangPai.isZipai(pai) && latestDaAction.getPai().equals(pai)
								&& player.getShoupaiCalculator().count(pai) == 1 && !gangmoShoupai.equals(pai)) {
							player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
						}
					}
					if (latestDaAction != null && MajiangPai.isZipai(gangmoShoupai)
							&& latestDaAction.getPai().equals(gangmoShoupai)
							&& player.getShoupaiCalculator().count(gangmoShoupai) == 0) {
						player.addActionCandidate(new MajiangDaAction(player.getId(), gangmoShoupai));
					}
				}
			}
		} else if (action.getType().equals(MajiangPlayerActionType.da)) {// 过的是别人打出牌之后我可以吃碰杠胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				RuianMajiangChiPengGangActionStatisticsListener chiPengGangRecordListener = ju
						.getActionStatisticsListenerManager()
						.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
				MajiangPlayerAction finallyDoneAction = chiPengGangRecordListener.findPlayerFinallyDoneAction();
				if (finallyDoneAction != null) {// 有其他吃碰杠动作，先执行吃碰杠
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰
						MajiangPengAction doAction = (MajiangPengAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangPengAction(doAction.getActionPlayerId(),
								doAction.getDachupaiPlayerId(), doAction.getPai()));
					} else if (finallyDoneAction instanceof MajiangGangAction) {// 如果是杠
						MajiangGangAction doAction = (MajiangGangAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangGangAction(doAction.getActionPlayerId(),
								doAction.getDachupaiPlayerId(), doAction.getPai(), doAction.getGangType()));
					} else if (finallyDoneAction instanceof MajiangChiAction) {// 如果是吃
						MajiangChiAction doAction = (MajiangChiAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangChiAction(doAction.getActionPlayerId(),
								doAction.getDachupaiPlayerId(), doAction.getChijinPai(), doAction.getShunzi()));
					}
				} else {
					// 打牌那家的下家摸牌
					MajiangPlayer xiajiaPlayer = currentPan
							.findXiajia(currentPan.findPlayerById(action.getActionPlayerId()));
					xiajiaPlayer.addActionCandidate(new MajiangMoAction(xiajiaPlayer.getId(), new LundaoMopai()));
				}
				chiPengGangRecordListener.updateForNextLun();// 清空动作缓存
			}
		} else if (action.getType().equals(MajiangPlayerActionType.gang)) {// 过的是别人杠牌之后我可以胡
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				// 杠牌那家摸牌
				MajiangPlayer gangPlayer = currentPan.findPlayerById(action.getActionPlayerId());
				gangPlayer.addActionCandidate(new MajiangMoAction(gangPlayer.getId(), new LundaoMopai()));
			}
		} else if (action.getType().equals(MajiangPlayerActionType.peng)) {// 过的是我碰了之后的杠
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 如果所有玩家啥也干不了
				// 那要我打牌
				List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
				RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
						.getActionStatisticsListenerManager()
						.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
				for (MajiangPai pai : fangruShoupaiList) {
					if (MajiangPai.isZipai(pai) && juezhangStatisticsListener.ifJuezhang(pai)) {
						player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
					}
				}
				if (player.getActionCandidates().isEmpty()) {
					player.generateDaActions();
				}
			}
		} else {
		}
	}

}
