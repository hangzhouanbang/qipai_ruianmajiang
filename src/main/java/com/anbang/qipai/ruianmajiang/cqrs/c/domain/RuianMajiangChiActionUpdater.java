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
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		Pan currentPan = ju.getCurrentPan();
		if (!juezhangStatisticsListener.getPlayerActionMap().isEmpty()) {// 如果有其他动作
			MajiangPlayerAction finallyDoneAction = juezhangStatisticsListener.findPlayerFinallyDoneAction();
			if (finallyDoneAction != null) {// 有其他碰杠动作，先执行碰杠
				MajiangPlayer actionPlayer = currentPan.findPlayerById(finallyDoneAction.getActionPlayerId());
				if (finallyDoneAction instanceof MajiangPengAction) {
					actionPlayer.addActionCandidate((MajiangPengAction) finallyDoneAction);
				} else if (finallyDoneAction instanceof MajiangGangAction) {
					actionPlayer.addActionCandidate((MajiangGangAction) finallyDoneAction);
				}
				juezhangStatisticsListener.updateForNextLun();
			} else {

			}
		} else {
			MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());
			currentPan.clearAllPlayersActionCandidates();

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
