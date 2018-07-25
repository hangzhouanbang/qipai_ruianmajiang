package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPengAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerPengActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangPengActionUpdater implements MajiangPlayerPengActionUpdater {// TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());

		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 胡
		RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangJuResultBuilder) ju.getJuResultBuilder();
		int dihu = ruianMajiangJuResultBuilder.getDihu();
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
		RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestChipenggangHu(dihu, gouXingPanHu, player,
				baibanIsGuipai);
		if (bestHu != null) {
			bestHu.setChipenggangAction(pengAction);
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

		// 碰的那个人要打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

	}

}
