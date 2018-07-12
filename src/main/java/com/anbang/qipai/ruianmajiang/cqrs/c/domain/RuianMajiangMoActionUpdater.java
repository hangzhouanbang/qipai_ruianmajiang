package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionUpdater;
import com.dml.majiang.Pan;

public class RuianMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());

		// 摸到公开牌了要补牌(继续摸牌)
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		if (baibanIsGuipai) {// 白板是鬼牌
			if (gangmoShoupai.equals(MajiangPai.hongzhong)) {// 红中是公开牌
				player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
			}
		} else {// 白板不是鬼牌
			if (gangmoShoupai.equals(MajiangPai.baiban)) { // 白板是公开牌
				player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
			}
		}

		// 有手牌或刻子可以杠这个摸来的牌
		player.tryShoupaigangmoAndGenerateCandidateAction();
		player.tryKezigangmoAndGenerateCandidateAction();

		// 杠四个手牌
		player.tryGangsigeshoupaiAndGenerateCandidateAction();

		// 可以胡
		// 非胡牌型特殊胡-三财神
		// int guipaiCount = player.countShoupaiGuipai();// TODO 可用鬼牌统计

		// 可胡牌型的胡

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// // TODO 啥也不能干，那只能打出牌
		// player.generateDaActions();
	}

}
