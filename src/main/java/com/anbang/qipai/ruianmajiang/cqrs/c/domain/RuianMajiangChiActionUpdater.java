package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangChiAction;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerChiActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangChiActionUpdater implements MajiangPlayerChiActionUpdater { // TODO 改成通用PengActionUpdater

	@Override
	public void updateActions(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();

		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 胡
		RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangJuResultBuilder) ju.getJuResultBuilder();
		int dihu = ruianMajiangJuResultBuilder.getDihu();
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
		RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestFeizimoHu(dihu, gouXingPanHu, player,
				baibanIsGuipai, chiAction.getChijinPai());
		if (bestHu != null) {
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

		// 吃的那个人要打出牌
		if (player.getActionCandidates().isEmpty()) {
			player.generateDaActions();
		}

		// TODO 接着做
	}

}
