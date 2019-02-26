package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.majiang.pan.result.CurrentPanResultBuilder;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.MajiangPlayer;

public class RuianMajiangPanResultBuilder implements CurrentPanResultBuilder {

	private int dihu;
	private boolean dapao;
	/**
	 * 封顶台数。0代表不封顶
	 */
	private int maxtai;

	@Override
	public PanResult buildCurrentPanResult(Ju ju, long panFinishTime) {
		Pan currentPan = ju.getCurrentPan();
		RuianMajiangPanResult latestFinishedPanResult = (RuianMajiangPanResult) ju.findLatestFinishedPanResult();
		Map<String, Integer> playerTotalScoreMap = new HashMap<>();
		if (latestFinishedPanResult != null) {
			for (RuianMajiangPanPlayerResult panPlayerResult : latestFinishedPanResult.getPanPlayerResultList()) {
				playerTotalScoreMap.put(panPlayerResult.getPlayerId(), panPlayerResult.getTotalScore());
			}
		}

		List<MajiangPlayer> huPlayers = currentPan.findAllHuPlayers();
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		if (huPlayers.size() > 0) {// 正常有人胡结束
			MajiangPlayer huPlayer = huPlayers.get(0);
			RuianMajiangHu hu = (RuianMajiangHu) huPlayer.getHu();
			String dianpaoPlayerId = hu.getDianpaoPlayerId();
			if (huPlayers.size() == 1) {// 一人胡

			} else {
				MajiangPlayer dianpaoPlayer = currentPan.findPlayerById(dianpaoPlayerId);
				dianpaoPlayer.setHu(null);// 财神吊时有其他人胡，其他人优先胡
				MajiangPlayer xiajiaPlayer = currentPan.findXiajia(dianpaoPlayer);
				// 按点炮者开始遍历出最先胡并把其他胡变为null
				boolean anyPlayerHu = false;
				while (true) {
					if (!xiajiaPlayer.getId().equals(dianpaoPlayerId)) {
						RuianMajiangHu hu1 = (RuianMajiangHu) xiajiaPlayer.getHu();
						if (!anyPlayerHu && hu1 != null) {
							huPlayer = xiajiaPlayer;
							hu = hu1;
							anyPlayerHu = true;
						} else {
							xiajiaPlayer.setHu(null);
						}
					} else {
						break;
					}
					xiajiaPlayer = currentPan.findXiajia(xiajiaPlayer);
				}
			}
			RuianMajiangPanPlayerScore huPlayerScore = hu.getScore();

			// 两两结算RuianMajiangPanPlayerScore
			List<RuianMajiangPanPlayerResult> playerResultList = new ArrayList<>();
			Collection<MajiangPlayer> players = currentPan.getMajiangPlayerIdMajiangPlayerMap().values();
			for (MajiangPlayer player : players) {
				RuianMajiangPanPlayerResult playerResult = new RuianMajiangPanPlayerResult(player.getId());
				if (player.getId().equals(huPlayer.getId())) {
					playerResult.setScore(huPlayerScore);
				} else {
					// 计算非胡玩家分数
					playerResult.setScore(RuianMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(dapao, dihu,
							maxtai, player, baibanIsGuipai));
				}
				playerResultList.add(playerResult);
			}

			for (int i = 0; i < playerResultList.size(); i++) {
				RuianMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
				RuianMajiangPanPlayerScore score1 = playerResult1.getScore();
				String playerId1 = playerResult1.getPlayerId();
				for (int j = (i + 1); j < playerResultList.size(); j++) {
					RuianMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
					RuianMajiangPanPlayerScore score2 = playerResult2.getScore();
					String playerId2 = playerResult2.getPlayerId();
					// 结算炮
					if (dapao) {
						score1.jiesuanPao(score2.getPao());
						score2.jiesuanPao(score1.getPao());
					}
					int jiesuanHushu = huPlayerScore.getHushu().quzhengValue();
					if (playerId1.equals(huPlayer.getId())) {// 1胡2不胡
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
								jiesuanHushu = quzheng(jiesuanHushu / 2);
								score1.jiesuanHushu(jiesuanHushu);
								score2.jiesuanHushu(jiesuanHushu * -1);
							}
						}
					} else if (playerId2.equals(huPlayer.getId())) {// 2胡1不胡
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
								jiesuanHushu = quzheng(jiesuanHushu / 2);
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
							int jiesuanHushu1 = hushu1.quzhengValue();
							RuianMajiangHushu hushu2 = score2.getHushu();
							int jiesuanHushu2 = hushu2.quzhengValue();
							int delta = jiesuanHushu1 - jiesuanHushu2;
							score1.jiesuanHushu(delta);
							score2.jiesuanHushu(delta * -1);
						} else {
							RuianMajiangHushu hushu1 = score1.getHushu();
							int jiesuanHushu1 = hushu1.quzhengValue();
							RuianMajiangHushu hushu2 = score2.getHushu();
							int jiesuanHushu2 = hushu2.quzhengValue();
							int delta = quzheng((jiesuanHushu1 - jiesuanHushu2) / 2);
							score1.jiesuanHushu(delta);
							score2.jiesuanHushu(delta * -1);
						}
					}
				}
			}

			// 胡的那家shoupaixing放入结果，其余不胡的shoupailist放入结果
			playerResultList.forEach((playerResult) -> {
				playerResult.getScore().jiesuan();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId())
							+ playerResult.getScore().getJiesuanScore());
				} else {
					playerResult.setTotalScore(playerResult.getScore().getJiesuanScore());
				}
			});

			RuianMajiangPanResult ruianMajiangPanResult = new RuianMajiangPanResult();
			ruianMajiangPanResult.setPan(new PanValueObject(currentPan));
			ruianMajiangPanResult.setPanFinishTime(panFinishTime);
			ruianMajiangPanResult.setPanPlayerResultList(playerResultList);
			ruianMajiangPanResult.setHu(true);
			ruianMajiangPanResult.setZimo(hu.isZimo());
			ruianMajiangPanResult.setDianpaoPlayerId(hu.getDianpaoPlayerId());
			return ruianMajiangPanResult;
		} else {// 流局

			List<RuianMajiangPanPlayerResult> playerResultList = new ArrayList<>();
			currentPan.getMajiangPlayerIdMajiangPlayerMap().values().forEach((player) -> {
				RuianMajiangPanPlayerResult playerResult = new RuianMajiangPanPlayerResult(player.getId());
				playerResult.setScore(RuianMajiangJiesuanCalculator.calculateBestScoreForBuhuPlayer(dapao, dihu, maxtai,
						player, baibanIsGuipai));
				playerResultList.add(playerResult);
			});
			if (dapao) {
				for (int i = 0; i < playerResultList.size(); i++) {
					RuianMajiangPanPlayerResult playerResult1 = playerResultList.get(i);
					RuianMajiangPanPlayerScore score1 = playerResult1.getScore();
					for (int j = (i + 1); j < playerResultList.size(); j++) {
						RuianMajiangPanPlayerResult playerResult2 = playerResultList.get(j);
						RuianMajiangPanPlayerScore score2 = playerResult2.getScore();
						// 结算炮
						score1.jiesuanPao(score2.getPao());
						score2.jiesuanPao(score1.getPao());
					}
				}
			}

			playerResultList.forEach((playerResult) -> {
				playerResult.getScore().jiesuan();
				// 计算累计总分
				if (latestFinishedPanResult != null) {
					playerResult.setTotalScore(playerTotalScoreMap.get(playerResult.getPlayerId())
							+ playerResult.getScore().getJiesuanScore());
				} else {
					playerResult.setTotalScore(playerResult.getScore().getJiesuanScore());
				}
			});

			RuianMajiangPanResult ruianMajiangPanResult = new RuianMajiangPanResult();
			ruianMajiangPanResult.setPan(new PanValueObject(currentPan));
			ruianMajiangPanResult.setPanFinishTime(panFinishTime);
			ruianMajiangPanResult.setPanPlayerResultList(playerResultList);
			ruianMajiangPanResult.setHu(false);

			return ruianMajiangPanResult;

		}

	}

	private int quzheng(int value) {
		int shang = value / 10;
		int yu = value % 10;
		if (yu > 0) {
			return (shang + 1) * 10;
		} else if (yu < 0) {
			return (shang - 1) * 10;
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

	public int getMaxtai() {
		return maxtai;
	}

	public void setMaxtai(int maxtai) {
		this.maxtai = maxtai;
	}

}
