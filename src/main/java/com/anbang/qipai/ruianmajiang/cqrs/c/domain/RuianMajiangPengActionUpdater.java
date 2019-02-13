package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		if (pengAction.isDisabledByHigherPriorityAction()) {// 如果动作被阻塞
			player.clearActionCandidates();// 玩家已经做了决定，要删除动作
			if (currentPan.allPlayerHasNoActionCandidates() && !currentPan.anyPlayerHu()) {// 所有玩家行牌结束，并且没人胡
				MajiangPlayerAction finallyDoneAction = juezhangStatisticsListener.findPlayerFinallyDoneAction();// 找出最终应该执行的动作
				MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
				if (finallyDoneAction instanceof MajiangPengAction) {// 如果是碰，也只能是碰
					MajiangPengAction action = (MajiangPengAction) finallyDoneAction;
					actionPlayer.addActionCandidate(new MajiangPengAction(action.getActionPlayerId(),
							action.getDachupaiPlayerId(), action.getPai()));
				}
				juezhangStatisticsListener.updateForNextLun();// 清空动作缓存
			}
		} else {
			currentPan.clearAllPlayersActionCandidates();
			juezhangStatisticsListener.updateForNextLun();// 清空动作缓存
			// 刻子杠手牌
			List<PengchuPaiZu> pengchupaiZuList = player.getPengchupaiZuList();
			PengchuPaiZu pengchuPaiZu = pengchupaiZuList.get(pengchupaiZuList.size() - 1);
			for (MajiangPai fangruShoupai : player.getFangruShoupaiList()) {
				if (pengchuPaiZu.getKezi().getPaiType().equals(fangruShoupai)) {
					player.addActionCandidate(new MajiangGangAction(pengAction.getActionPlayerId(),
							pengAction.getDachupaiPlayerId(), fangruShoupai, GangType.kezigangshoupai));
					break;
				}
			}

			// 需要有“过”
			player.checkAndGenerateGuoCandidateAction();
			if (player.getActionCandidates().isEmpty()) {
				List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
				for (MajiangPai pai : fangruShoupaiList) {
					if (MajiangPai.isZipai(pai) && juezhangStatisticsListener.ifJuezhang(pai)) {
						player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
					}
				}
			}
			if (player.getActionCandidates().isEmpty()) {
				player.generateDaActions();
			}
		}
	}

}
