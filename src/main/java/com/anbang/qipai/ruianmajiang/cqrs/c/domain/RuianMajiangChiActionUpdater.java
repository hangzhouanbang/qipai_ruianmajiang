package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.MajiangPlayerChiActionUpdater;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.comprehensive.JuezhangStatisticsListener;

/**
 * 吃的那个人要打牌,有风字绝张优先打
 * 
 * @author neo
 *
 */
public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater {

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();

		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());

		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		JuezhangStatisticsListener juezhangStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(JuezhangStatisticsListener.class);
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
