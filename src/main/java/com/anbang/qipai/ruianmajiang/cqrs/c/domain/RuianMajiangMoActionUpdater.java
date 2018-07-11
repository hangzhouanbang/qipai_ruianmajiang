package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		player.clearActionCandidates();

		// TODO 庄家摸第一张牌直接放手牌 ？那直接4个一样怎么办？ 新加杠四个手牌类型？ 摸第一张牌是公开牌直接放公开牌列表 ？

		// TODO 摸来的是公开牌

		// 有手牌或刻子可以杠这个摸来的牌
		player.tryShoupaigangmoAndGenerateCandidateAction();
		player.tryKezigangmoAndGenerateCandidateAction();

		// 可以胡
		// TODO 非胡牌型特殊胡

		// 可胡牌型的胡

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// // TODO 啥也不能干，那只能打出牌
		// player.generateDaActions();
	}

}
