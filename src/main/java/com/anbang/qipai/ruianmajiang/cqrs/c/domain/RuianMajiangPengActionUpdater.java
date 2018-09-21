package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener.RuianMajiangChiPengGangActionStatisticsListener;
import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.peng.MajiangPengAction;
import com.dml.majiang.player.action.peng.MajiangPlayerPengActionUpdater;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		RuianMajiangChiPengGangActionStatisticsListener juezhangStatisticsListener = ju
				.getActionStatisticsListenerManager()
				.findListener(RuianMajiangChiPengGangActionStatisticsListener.class);
		if (juezhangStatisticsListener.getPlayerActionMap().containsKey(pengAction.getActionPlayerId())) {

		} else {
			Pan currentPan = ju.getCurrentPan();
			currentPan.clearAllPlayersActionCandidates();
			MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

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
