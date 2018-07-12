package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionProcessor;
import com.dml.majiang.Pan;

public class RuianMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());

		// 摸牌理由如果是补牌,那上一次摸的要放入公开牌列表
		if (action.getReason().getName().equals(RuianBupai.name)) {
			player.fangruPublicPai();
		}

		currentPan.playerMoPai(action.getActionPlayerId());
		currentPan.setActivePaiCursor(null);
	}

}
