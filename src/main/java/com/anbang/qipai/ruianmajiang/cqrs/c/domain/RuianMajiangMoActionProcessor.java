package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionProcessor;
import com.dml.majiang.Pan;

public class RuianMajiangMoActionProcessor implements MajiangPlayerMoActionProcessor {

	@Override
	public void process(MajiangMoAction action, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<MajiangPai> avaliablePaiList = currentPan.getAvaliablePaiList();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		MajiangPlayer player = currentPan.findPlayerById(action.getActionPlayerId());
		moPai(avaliablePaiList, player, baibanIsGuipai);
		currentPan.setActivePaiCursor(null);
	}

	private void moPai(List<MajiangPai> avaliablePaiList, MajiangPlayer player, boolean baibanIsGuipai) {
		MajiangPai pai = avaliablePaiList.remove(0);
		if (baibanIsGuipai) {// 白板是鬼牌
			if (pai.equals(MajiangPai.hongzhong)) {// 红中公示
				player.addPublicPai(pai);
				moPai(avaliablePaiList, player, baibanIsGuipai);
			} else {
				player.moPai(pai);
			}
		} else {// 白板不是鬼牌
			if (pai.equals(MajiangPai.baiban)) { // 白板公示
				player.addPublicPai(pai);
				moPai(avaliablePaiList, player, baibanIsGuipai);
			} else {
				player.moPai(pai);
			}
		}
	}

}
