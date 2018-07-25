package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.GanghouBupai;
import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangGangAction;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerGangActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangGangActionUpdater implements MajiangPlayerGangActionUpdater {// TODO 改成通用GangActionUpdater

	@Override
	public void updateActions(MajiangGangAction gangAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());

		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 胡
		RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangJuResultBuilder) ju.getJuResultBuilder();
		int dihu = ruianMajiangJuResultBuilder.getDihu();
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
		RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestChipenggangHu(dihu, gouXingPanHu, player,
				baibanIsGuipai);
		if (bestHu != null) {
			bestHu.setChipenggangAction(gangAction);
			player.addActionCandidate(new MajiangHuAction(player.getId(), bestHu));
		} else {
			// // 非胡牌型特殊胡-三财神
			// MoGuipaiCounter moGuipaiCounter =
			// ju.getActionStatisticsListenerManager().findListener(MoGuipaiCounter.class);
			// if (moGuipaiCounter.getCount() == 3) {
			//
			// }
		}

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// 杠完之后要摸牌
		if (player.getActionCandidates().isEmpty()) {
			player.addActionCandidate(new MajiangMoAction(player.getId(),
					new GanghouBupai(gangAction.getPai(), gangAction.getGangType())));
		}
	}

}
