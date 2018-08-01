package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.action.mo.MajiangPlayerMoActionProcessor;

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
