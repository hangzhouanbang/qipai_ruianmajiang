package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionUpdater;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;

/**
 * 吃的那个人要打牌,有风字绝张优先打
 * 
 * @author neo
 *
 */
public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater {

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());
		RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		if (chiAction.isDisabledByHigherPriorityAction()) {// 如果动作被阻塞
			player.clearActionCandidates();// 玩家已经做了决定，要删除动作
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 所有玩家行牌结束，并且没人胡
				MajiangPlayerAction finallyDoneAction = juezhangStatisticsListener.findPlayerFinallyDoneAction();// 找出最终应该执行的动作
				if (finallyDoneAction != null) {
					MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
					if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰
						MajiangPengAction action = (MajiangPengAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangPengAction(action.getActionPlayerId(),
								action.getDachupaiPlayerId(), action.getPai()));
					} else if (finallyDoneAction instanceof MajiangGangAction) {// 如果是杠
						MajiangGangAction action = (MajiangGangAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangGangAction(action.getActionPlayerId(),
								action.getDachupaiPlayerId(), action.getPai(), action.getGangType()));
					} else if (finallyDoneAction instanceof MajiangChiAction) {// 如果是吃
						MajiangChiAction action = (MajiangChiAction) finallyDoneAction;
						actionPlayer.addActionCandidate(new MajiangChiAction(action.getActionPlayerId(),
								action.getDachupaiPlayerId(), action.getChijinPai(), action.getShunzi()));
					}
				} else {

				}
				juezhangStatisticsListener.updateForNextLun();// 清空动作缓存
			}
		} else {
			currentPan.clearAllPlayersActionCandidates();
			juezhangStatisticsListener.updateForNextLun();// 清空动作缓存

			List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
			for (MajiangPai pai : fangruShoupaiList) {
				if (MajiangPai.isZipai(pai) && juezhangStatisticsListener.ifJuezhang(pai)) {
					player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
				}
			}
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
