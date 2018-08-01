package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.fapai.FaPaiStrategy;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;

/**
 * 顺序发牌。白板如果不是鬼牌的话，拿出公示。白板是鬼牌那改为红中公示
 * 
 * @author Neo
 *
 */
public class RuianMajiangFaPaiStrategy implements FaPaiStrategy {

	private int faPaiCountsForOnePlayer;

	public RuianMajiangFaPaiStrategy() {
	}

	public RuianMajiangFaPaiStrategy(int faPaiCountsForOnePlayer) {
		this.faPaiCountsForOnePlayer = faPaiCountsForOnePlayer;
	}

	@Override
	public void faPai(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		List<MajiangPai> avaliablePaiList = currentPan.getAvaliablePaiList();
		MajiangPosition zhuangPlayerMenFeng = currentPan.findMenFengForZhuang();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		for (int i = 0; i < faPaiCountsForOnePlayer; i++) {
			MajiangPosition playerMenFeng = zhuangPlayerMenFeng;
			for (int j = 0; j < 4; j++) {
				MajiangPlayer player = currentPan.findPlayerByMenFeng(playerMenFeng);
				if (player != null) {
					faPai(avaliablePaiList, player, baibanIsGuipai);
				}
				playerMenFeng = MajiangPositionUtil.nextPositionAntiClockwise(playerMenFeng);
			}
		}
	}

	private void faPai(List<MajiangPai> avaliablePaiList, MajiangPlayer player, boolean baibanIsGuipai) {
		MajiangPai pai = avaliablePaiList.remove(0);
		if (baibanIsGuipai) {// 白板是鬼牌
			if (pai.equals(MajiangPai.hongzhong)) {// 红中公示
				player.addPublicPai(pai);
				faPai(avaliablePaiList, player, baibanIsGuipai);
			} else {
				player.addShoupai(pai);
			}
		} else {// 白板不是鬼牌
			if (pai.equals(MajiangPai.baiban)) { // 白板公示
				player.addPublicPai(pai);
				faPai(avaliablePaiList, player, baibanIsGuipai);
			} else {
				player.addShoupai(pai);
			}
		}
	}

	public int getFaPaiCountsForOnePlayer() {
		return faPaiCountsForOnePlayer;
	}

	public void setFaPaiCountsForOnePlayer(int faPaiCountsForOnePlayer) {
		this.faPaiCountsForOnePlayer = faPaiCountsForOnePlayer;
	}

}
