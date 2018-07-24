package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.Ju;
import com.dml.majiang.MajiangHuAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerHuActionProcessor;
import com.dml.majiang.Pan;

public class RuianMajiangHuActionProcessor implements MajiangPlayerHuActionProcessor {

	@Override
	public void process(MajiangHuAction action, Ju ju) throws Exception {
		RuianMajiangHu hu = (RuianMajiangHu) action.getHu();
		RuianMajiangHushu huPlayerHushu = hu.getHushu();
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer huPlayer = currentPan.findPlayerById(action.getActionPlayerId());
		RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = (RuianMajiangJuResultBuilder) ju.getJuResultBuilder();
		int dihu = ruianMajiangJuResultBuilder.getDihu();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 两两结算RuianMajiangPanPlayerScore
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<RuianMajiangPanPlayerScore> playerScoreList = new ArrayList<>();
		playerIdList.forEach((playerId) -> {
			RuianMajiangPanPlayerScore score = new RuianMajiangPanPlayerScore();
			score.setPlayerId(playerId);
			if (playerId.equals(huPlayer.getId())) {
				score.setHushu(huPlayerHushu);
			} else {
				// 计算非胡玩家胡数
				score.setHushu(RuianMajiangJiesuanCalculator.calculateBestHushuForBuhuPlayer(dihu,
						currentPan.findPlayerById(playerId), baibanIsGuipai));
			}
			playerScoreList.add(score);
		});

		for (int i = 0; i < playerScoreList.size(); i++) {
			RuianMajiangPanPlayerScore score1 = playerScoreList.get(i);
			String playerId1 = score1.getPlayerId();
			for (int j = (i + 1); j < playerScoreList.size(); j++) {
				RuianMajiangPanPlayerScore score2 = playerScoreList.get(j);
				String playerId2 = score2.getPlayerId();
				if (playerId1.equals(huPlayer.getId())) {// 1胡2不胡
					int jiesuanHushu = quzheng(huPlayerHushu.getValue());
					// 是不是庄家胡
					boolean zhuangHu = currentPan.getZhuangPlayerId().equals(playerId1);
					if (zhuangHu) {// 闲家输庄家
						score1.jiesuanHushu(jiesuanHushu);
						score2.jiesuanHushu(jiesuanHushu * -1);
					} else {
						// 是不是庄家输
						boolean zhuangShu = currentPan.getZhuangPlayerId().equals(playerId2);
						if (zhuangShu) {// 庄家输闲家
							score1.jiesuanHushu(jiesuanHushu);
							score2.jiesuanHushu(jiesuanHushu * -1);
						} else {// 闲家输闲家
							jiesuanHushu = quzheng(huPlayerHushu.getValue() / 2);
							score1.jiesuanHushu(jiesuanHushu);
							score2.jiesuanHushu(jiesuanHushu * -1);
						}
					}
				} else if (playerId2.equals(huPlayer.getId())) {// 2胡1不胡
					int jiesuanHushu = quzheng(huPlayerHushu.getValue());
					// 是不是庄家胡
					boolean zhuangHu = currentPan.getZhuangPlayerId().equals(playerId2);
					if (zhuangHu) {// 闲家输庄家
						score2.jiesuanHushu(jiesuanHushu);
						score1.jiesuanHushu(jiesuanHushu * -1);
					} else {
						// 是不是庄家输
						boolean zhuangShu = currentPan.getZhuangPlayerId().equals(playerId1);
						if (zhuangShu) {// 庄家输闲家
							score2.jiesuanHushu(jiesuanHushu);
							score1.jiesuanHushu(jiesuanHushu * -1);
						} else {// 闲家输闲家
							jiesuanHushu = quzheng(huPlayerHushu.getValue() / 2);
							score2.jiesuanHushu(jiesuanHushu);
							score1.jiesuanHushu(jiesuanHushu * -1);
						}
					}
				} else {// 不胡之间
					// 是否庄闲结算
					boolean zhuangxianjiesuan = currentPan.getZhuangPlayerId().equals(playerId1)
							|| currentPan.getZhuangPlayerId().equals(playerId2);
					if (zhuangxianjiesuan) {
						RuianMajiangHushu hushu1 = score1.getHushu();
						int jiesuanHushu1 = quzheng(hushu1.getValue());
						RuianMajiangHushu hushu2 = score2.getHushu();
						int jiesuanHushu2 = quzheng(hushu2.getValue());
						int delta = jiesuanHushu1 - jiesuanHushu2;
						score1.jiesuanHushu(delta);
						score2.jiesuanHushu(delta * -1);
					} else {
						RuianMajiangHushu hushu1 = score1.getHushu();
						int jiesuanHushu1 = quzheng(hushu1.getValue());
						RuianMajiangHushu hushu2 = score2.getHushu();
						int jiesuanHushu2 = quzheng(hushu2.getValue());
						int delta = quzheng((jiesuanHushu1 - jiesuanHushu2) / 2);
						score1.jiesuanHushu(delta);
						score2.jiesuanHushu(delta * -1);
					}
				}
			}
		}
		playerScoreList.forEach((score) -> score.jiesuan());

		// List<RuianMajiangPanPlayerScore> playerScoreList = new ArrayList<>();
		RuianMajiangPanResult ruianMajiangPanResult = new RuianMajiangPanResult();
		ruianMajiangPanResult.setPlayerScoreList(playerScoreList);
		currentPan.setResult(ruianMajiangPanResult);

	}

	private int quzheng(int value) {
		int shang = value / 10;
		int yu = value % 10;
		if (yu > 0) {
			return (shang + 1) * 10;
		} else {
			return shang * 10;
		}
	}

}
