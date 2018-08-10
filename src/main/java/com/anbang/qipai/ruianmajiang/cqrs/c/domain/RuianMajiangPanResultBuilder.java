package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;

public class RuianMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private int dihu;
	private boolean dapao;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		RuianMajiangPanResult latestFinishedPanResult = (RuianMajiangPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (RuianMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		MajiangPlayer huPlayer = currentPan.findHuPlayer();
		// TODO 要处理不胡,流局
		RuianMajiangHu hu = (RuianMajiangHu) huPlayer.getHu();
		RuianMajiangPanPlayerScore huPlayerScore = hu.getScore();
		ShoupaiPaiXing huShoupaiPaiXing = hu.getShoupaiPaiXing();

		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 两两结算RuianMajiangPanPlayerScore
		List<String> playerIdList = currentPan.sortedPlayerIdList();
		List<RuianMajiangPanPlayerResult> playerResultList = new ArrayList<>();
		playerIdList.forEach((playerId) -> {
			RuianMajiangPanPlayerResult playerResult = new RuianMajiangPanPlayerResult();
			playerResult.setPlayerId(playerId);
			if (playerId.equals(huPlayer.getId())) {
				playerResult.setScore(huPlayerScore);
			} else {
				// 计算非胡玩家分数
				playerResult.setScore(RuianMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(dapao, dihu,
						currentPan.findPlayerById(playerId), baibanIsGuipai));
			}
			playerResultList.add(playerResult);
		});

		for (int i = 0; i < playerResultList.size(); i++) {
			RuianMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
			RuianMajiangPanPlayerScore score1 = playerResult1.getScore();
			String playerId1 = playerResult1.getPlayerId();
			for (int j = (i + 1); j < playerResultList.size(); j++) {
				RuianMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
				RuianMajiangPanPlayerScore score2 = playerResult2.getScore();
				String playerId2 = playerResult2.getPlayerId();
				if (playerId1.equals(huPlayer.getId())) {// 1胡2不胡
					int jiesuanHushu = quzheng(huPlayerScore.getValue());
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
							jiesuanHushu = quzheng(huPlayerScore.getValue() / 2);
							score1.jiesuanHushu(jiesuanHushu);
							score2.jiesuanHushu(jiesuanHushu * -1);
						}
					}
				} else if (playerId2.equals(huPlayer.getId())) {// 2胡1不胡
					int jiesuanHushu = quzheng(huPlayerScore.getValue());
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
							jiesuanHushu = quzheng(huPlayerScore.getValue() / 2);
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

		// 胡的那家shoupaixing放入结果，其余不胡的shoupailist放入结果
		playerResultList.forEach((playerResult) -> {
			MajiangPlayer player = currentPan.findPlayerById(playerResult.getPlayerId());
			playerResult.getScore().jiesuan();
			// 计算累计总分
			if (latestFinishedPanResult != null) {
				playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId())
						+ playerResult.getScore().getJiesuanScore());
			} else {
				playerResult.setTotalScore(playerResult.getScore().getJiesuanScore());
			}
			playerResult.setMenFeng(player.getMenFeng());
			// 吃碰杠出去的要加到结果
			playerResult.setPublicPaiList(new ArrayList<>(player.getPublicPaiList()));
			playerResult.setChichupaiZuList(new ArrayList<>(player.getChichupaiZuList()));
			playerResult.setPengchupaiZuList(new ArrayList<>(player.getPengchupaiZuList()));
			playerResult.setGangchupaiZuList(new ArrayList<>(player.getGangchupaiZuList()));
			playerResult.setGuipaiTypeSet(new HashSet<>(player.getGuipaiTypeSet()));
			playerResult.setShoupaiList(new ArrayList<>(player.getFangruShoupaiList()));
			if (playerResult.getPlayerId().equals(huPlayer.getId())) {
				playerResult.setHu(true);
				playerResult.setBestShoupaiPaiXing(huShoupaiPaiXing);
			} else {
				playerResult.setHu(false);
			}
		});

		RuianMajiangPanResult ruianMajiangPanResult = new RuianMajiangPanResult();
		ruianMajiangPanResult.setPanNo(currentPan.getNo());
		ruianMajiangPanResult.setPanFinishTime(panFinishTime);
		ruianMajiangPanResult.setZhuangPlayerId(currentPan.getZhuangPlayerId());
		ruianMajiangPanResult.setPlayerResultList(playerResultList);
		ruianMajiangPanResult.setHu(true);
		ruianMajiangPanResult.setZimo(hu.isZimo());
		ruianMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
		return ruianMajiangPanResult;
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

	public int getDihu() {
		return dihu;
	}

	public void setDihu(int dihu) {
		this.dihu = dihu;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

}
