package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.finish.CurrentPanFinishiDeterminer;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;

public class RuianMajiangPanFinishiDeterminer implements CurrentPanFinishiDeterminer {

	@Override
	public boolean determineToFinishCurrentPan(Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		boolean hu = currentPan.anyPlayerHu();
		// 当有人胡并且所有人没有胡的动作游戏结束
		if (hu) {
			List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
			Map<String, MajiangPlayer> huPlayerMap = new HashMap<>();
			Set<String> huPlayerIdSet = new HashSet<>();
			for (MajiangPlayer huPlayer : huPlayers) {
				huPlayerIdSet.add(huPlayer.getId());
				huPlayerMap.put(huPlayer.getId(), huPlayer);
			}
			MajiangPlayer bestHuPlayer = huPlayers.get(0);
			RuianMajiangHu bestHu = (RuianMajiangHu) bestHuPlayer.getHu();
			String dianpaoPlayerId = bestHu.getDianpaoPlayerId();
			if (dianpaoPlayerId != null) {// 如果是财神吊或者点炮胡有可能有多个胡家
				MajiangPlayer dianpaoPlayer = currentPan.findPlayerById(dianpaoPlayerId);
				MajiangPlayer xiajiaPlayer = currentPan.findXiajia(dianpaoPlayer);
				boolean anyPlayerHu = false;// 是否有人胡
				while (true) {
					if (!xiajiaPlayer.getId().equals(dianpaoPlayerId)) {
						MajiangPlayer huPlayer = huPlayerMap.get(xiajiaPlayer.getId());
						if (!anyPlayerHu && huPlayer != null) {// 已经点了"胡"
							bestHuPlayer = huPlayer;
							anyPlayerHu = true;
						} else if (!anyPlayerHu) {// 没有点"胡"或者选了"过"
							for (MajiangPlayerAction action : xiajiaPlayer.getActionCandidates().values()) {
								if (action.getType().equals(MajiangPlayerActionType.hu)) {
									bestHuPlayer = xiajiaPlayer;
									anyPlayerHu = true;
								}
							}
						}
					} else {
						break;
					}
					xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
				}
				if (huPlayerIdSet.contains(bestHuPlayer.getId())) {
					return true;
				}
			} else {
				return true;
			}
			return false;
		} else {
			int liupai = 14;
			RuianMajiangChiPengGangActionStatisticsListener gangCounter = ju.getActionStatisticsListenerManager()
					.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
			if (gangCounter.getCount() > 0) {
				liupai += (4 + (gangCounter.getCount() - 1) * 2);
			}

			int avaliablePaiLeft = currentPan.countAvaliablePai();

			if (avaliablePaiLeft <= liupai && currentPan.allPlayerHasNoHuActionCandidates()) {
				return true;
			} else {
				return false;
			}

		}

	}

}
