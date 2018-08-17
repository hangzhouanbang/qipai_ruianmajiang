package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.comprehensive.JuezhangStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

		List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
		JuezhangStatisticsListener juezhangStatisticsListener = ju.getActionStatisticsListenerManager()
				.findListener(JuezhangStatisticsListener.class);
		for (MajiangPai pai : fangruShoupaiList) {
			if (juezhangStatisticsListener.ifJuezhang(pai)) {
				player.addActionCandidate(new MajiangDaAction(player.getId(), pai));
			}
		}
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

	}

}
