package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.mo.GanghouBupai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.GuipaiDangPai;
import com.dml.majiang.player.shoupai.PaiXing;
import com.dml.majiang.player.shoupai.ShoupaiCalculator;
import com.dml.majiang.player.shoupai.ShoupaiDuiziZu;
import com.dml.majiang.player.shoupai.ShoupaiGangziZu;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.player.shoupai.ShoupaiShunziZu;
import com.dml.majiang.player.shoupai.ShoupaiWithGuipaiDangGouXingZu;
import com.dml.majiang.player.shoupai.gouxing.GouXing;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

public class RuianMajiangJiesuanCalculator {

	// 自摸胡
	public static RuianMajiangHu calculateBestZimoHu(boolean couldSiFengQi, boolean couldTianhu, boolean dapao,
			int dihu, int maxtai, GouXingPanHu gouXingPanHu, MajiangPlayer player, MajiangMoAction moAction,
			boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		if (!player.gangmoGuipai()) {
			shoupaiCalculator.addPai(player.getGangmoShoupai());
		}
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, player.getGangmoShoupai());
		if (!player.gangmoGuipai()) {
			shoupaiCalculator.removePai(player.getGangmoShoupai());
		}

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player,
					null);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(couldSiFengQi, false, couldTianhu,
						false, shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true,
						moAction.getReason().getName().equals(GanghouBupai.name), true, false, dihu, maxtai, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				} else if (bestScore.getValue() == score.getValue()) {
					if (bestScore.getHushu().getTaishu().getValue() < score.getHushu().getTaishu().getValue()) {
						bestScore = score;
						bestHuShoupaiPaiXing = shoupaiPaiXing;
					} else if (bestScore.getHushu().getTaishu().getValue() == score.getHushu().getTaishu().getValue()) {
						if (bestScore.getHushu().getValue() < score.getHushu().getValue()) {
							bestScore = score;
							bestHuShoupaiPaiXing = shoupaiPaiXing;
						}
					}
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	// 抢杠胡
	public static RuianMajiangHu calculateBestQianggangHu(boolean couldSiFengQi, MajiangPai gangPai, boolean dapao,
			int dihu, int maxtai, GouXingPanHu gouXingPanHu, MajiangPlayer player, boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做
		shoupaiCalculator.addPai(gangPai);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, gangPai);
		shoupaiCalculator.removePai(gangPai);
		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player,
					gangPai);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(couldSiFengQi, true, false, false,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true, false, true, true, dihu, maxtai, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				} else if (bestScore.getValue() == score.getValue()) {
					if (bestScore.getHushu().getTaishu().getValue() < score.getHushu().getTaishu().getValue()) {
						bestScore = score;
						bestHuShoupaiPaiXing = shoupaiPaiXing;
					} else if (bestScore.getHushu().getTaishu().getValue() == score.getHushu().getTaishu().getValue()) {
						if (bestScore.getHushu().getValue() < score.getHushu().getValue()) {
							bestScore = score;
							bestHuShoupaiPaiXing = shoupaiPaiXing;
						}
					}
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	// 点炮胡
	public static RuianMajiangHu calculateBestDianpaoHu(boolean couldSiFengQi, boolean couldDihu, boolean dapao,
			int dihu, int maxtai, GouXingPanHu gouXingPanHu, MajiangPlayer player, boolean baibanIsGuipai,
			MajiangPai hupai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, hupai);

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player,
					hupai);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(couldSiFengQi, true, false,
						couldDihu, shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true, false, false, false, dihu,
						maxtai, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				} else if (bestScore.getValue() == score.getValue()) {
					if (bestScore.getHushu().getTaishu().getValue() < score.getHushu().getTaishu().getValue()) {
						bestScore = score;
						bestHuShoupaiPaiXing = shoupaiPaiXing;
					} else if (bestScore.getHushu().getTaishu().getValue() == score.getHushu().getTaishu().getValue()) {
						if (bestScore.getHushu().getValue() < score.getHushu().getValue()) {
							bestScore = score;
							bestHuShoupaiPaiXing = shoupaiPaiXing;
						}
					}
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	public static RuianMajiangPanPlayerScore calculateBestScoreForBuhuPlayer(boolean dapao, int dihu, int maxtai,
			MajiangPlayer player, boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> shoupaiPaiXingList = calculateBuhuShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator);

		// 要选出分数最高的牌型
		// 先计算和手牌型无关的参数
		ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player,
				null);
		RuianMajiangPanPlayerScore bestScore = null;
		for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
			RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(false, false, false, false,
					shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, false, false, false, false, dihu, maxtai, dapao);
			if (bestScore == null || bestScore.getValue() < score.getValue()) {
				bestScore = score;
			} else if (bestScore.getValue() == score.getValue()) {
				if (bestScore.getHushu().getTaishu().getValue() < score.getHushu().getTaishu().getValue()) {
					bestScore = score;
				} else if (bestScore.getHushu().getTaishu().getValue() == score.getHushu().getTaishu().getValue()) {
					if (bestScore.getHushu().getValue() < score.getHushu().getValue()) {
						bestScore = score;
					}
				}
			}
		}
		return bestScore;
	}

	public static RuianMajiangPanPlayerScore calculateBestScoreForTuidaohuPlayer(boolean dapao, int dihu, int maxtai,
			MajiangPlayer player, boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> shoupaiPaiXingList = calculateBuhuShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator);

		// 要选出分数最高的牌型
		// 先计算和手牌型无关的参数
		ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player,
				null);
		RuianMajiangPanPlayerScore bestScore = null;
		for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
			RuianMajiangPanPlayerScore score = calculateTuidaohuScoreForShoupaiPaiXing(false, false, false, false,
					shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, false, false, false, false, dihu, maxtai, dapao);
			if (bestScore == null || bestScore.getValue() < score.getValue()) {
				bestScore = score;
			} else if (bestScore.getValue() == score.getValue()) {
				if (bestScore.getHushu().getTaishu().getValue() < score.getHushu().getTaishu().getValue()) {
					bestScore = score;
				} else if (bestScore.getHushu().getTaishu().getValue() == score.getHushu().getTaishu().getValue()) {
					if (bestScore.getHushu().getValue() < score.getHushu().getValue()) {
						bestScore = score;
					}
				}
			}
		}
		return bestScore;
	}

	private static RuianMajiangPanPlayerScore calculateTuidaohuScoreForShoupaiPaiXing(boolean couldSiFengQi,
			boolean dianpao, boolean couldTianhu, boolean couldDihu,
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu, ShoupaiPaiXing shoupaiPaiXing, boolean hu,
			boolean gangkaiHu, boolean zimoHu, boolean qianggangHu, int dihu, int maxtai, boolean dapao) {
		RuianMajiangPanPlayerScore score = new RuianMajiangPanPlayerScore();
		RuianMajiangHushu hushu = new RuianMajiangHushu();
		RuianMajiangTaishu taishu = new RuianMajiangTaishu();
		if (shoupaixingWuguanJiesuancanshu.getBaibanShu() > 0 || shoupaixingWuguanJiesuancanshu.getFacaiShu() > 1
				|| shoupaixingWuguanJiesuancanshu.getHongzhongShu() > 1
				|| shoupaixingWuguanJiesuancanshu.getMenFengPaiShu() > 1
				|| shoupaixingWuguanJiesuancanshu.isGangchuFacai() || shoupaixingWuguanJiesuancanshu.isPengchuFacai()
				|| shoupaixingWuguanJiesuancanshu.isGangchuHongzhong()
				|| shoupaixingWuguanJiesuancanshu.isPengchuHongzhong() || shoupaixingWuguanJiesuancanshu.isZuofengGang()
				|| shoupaixingWuguanJiesuancanshu.isZuofengPeng()) {
			hushu.setValue(500);
		} else {
			hushu.setValue(250);
		}
		taishu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		taishu.setValue(3);
		hushu.setTaishu(taishu);
		score.setHushu(hushu);
		if (dapao) {
			RuianMajiangPao pao = calculatePao(shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, hu);
			score.setPao(pao);
		}
		score.calculate();
		return score;
	}

	private static RuianMajiangPanPlayerScore calculateScoreForShoupaiPaiXing(boolean couldSiFengQi, boolean dianpao,
			boolean couldTianhu, boolean couldDihu, ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu,
			ShoupaiPaiXing shoupaiPaiXing, boolean hu, boolean gangkaiHu, boolean zimoHu, boolean qianggangHu, int dihu,
			int maxtai, boolean dapao) {
		RuianMajiangPanPlayerScore score = new RuianMajiangPanPlayerScore();
		RuianMajiangHushu hushu = calculateHushu(couldSiFengQi, dianpao, couldTianhu, couldDihu, hu, gangkaiHu, zimoHu,
				qianggangHu, dihu, maxtai, shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
		score.setHushu(hushu);
		if (dapao) {
			RuianMajiangPao pao = calculatePao(shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, hu);
			score.setPao(pao);
		}
		score.calculate();
		return score;
	}

	private static RuianMajiangPao calculatePao(ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu,
			ShoupaiPaiXing shoupaiPaiXing, boolean hu) {
		RuianMajiangPao pao = new RuianMajiangPao();
		pao.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		pao.setCaishenShu(shoupaixingWuguanJiesuancanshu.getCaishenShu());
		pao.setFacaiAnke(shoupaiPaiXing.hasYuanPaiKeziZuForPaiType(MajiangPai.facai));
		pao.setFacaiGang(shoupaixingWuguanJiesuancanshu.isGangchuFacai());
		pao.setFacaiPeng(shoupaixingWuguanJiesuancanshu.isPengchuFacai());
		pao.setHongzhongAnke(shoupaiPaiXing.hasYuanPaiKeziZuForPaiType(MajiangPai.hongzhong));

		pao.setHongzhongGang(shoupaixingWuguanJiesuancanshu.isGangchuHongzhong());
		pao.setHongzhongPeng(shoupaixingWuguanJiesuancanshu.isPengchuHongzhong());
		pao.setHu(hu);

		pao.setZuofengAnke(shoupaiPaiXing.hasYuanPaiKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai()));

		pao.setZuofengGang(shoupaixingWuguanJiesuancanshu.isZuofengGang());
		pao.setZuofengPeng(shoupaixingWuguanJiesuancanshu.isZuofengPeng());

		pao.calculate();

		return pao;
	}

	private static RuianMajiangHushu calculateHushu(boolean couldSiFengQi, boolean dianpao, boolean couldTianhu,
			boolean couldDihu, boolean hu, boolean gangkaiHu, boolean zimoHu, boolean qianggangHu, int dihu, int maxtai,
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu, ShoupaiPaiXing shoupaiPaiXing) {
		RuianMajiangHushu hushu = new RuianMajiangHushu();
		hushu.setDihu(dihu);
		RuianMajiangTaishu taishu = new RuianMajiangTaishu();
		taishu.setMaxtai(maxtai);
		hushu.setTaishu(taishu);
		taishu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		taishu.setDanzhangdiaoHu(hu && shoupaixingWuguanJiesuancanshu.getFangruShoupaiCount() == 1);
		taishu.setDiHu(couldDihu);
		int shoupaiShunziCount = shoupaiPaiXing.countShunzi();
		if (hu) {
			taishu.setDuiduiHu(shoupaixingWuguanJiesuancanshu.getChichupaiZuCount() == 0 && shoupaiShunziCount == 0);
		}
		boolean facaiAnke = false;
		List<ShoupaiKeziZu> allKeziZuForFacai = shoupaiPaiXing.findAllKeziZuForPaiType(MajiangPai.facai);
		for (ShoupaiKeziZu shoupaiKeziZu : allKeziZuForFacai) {
			if (shoupaiKeziZu.countDangPai(GuipaiDangPai.dangType) < 2) {
				facaiAnke = true;
				break;
			}
		}
		taishu.setFacaiAnke(facaiAnke);
		taishu.setFacaiGang(shoupaixingWuguanJiesuancanshu.isGangchuFacai());
		taishu.setFacaiPeng(shoupaixingWuguanJiesuancanshu.isPengchuFacai());
		taishu.setGangkaiHu(gangkaiHu);
		boolean hongzhongAnke = false;
		List<ShoupaiKeziZu> allKeziZuForHongzhong = shoupaiPaiXing.findAllKeziZuForPaiType(MajiangPai.hongzhong);
		for (ShoupaiKeziZu shoupaiKeziZu : allKeziZuForHongzhong) {
			if (shoupaiKeziZu.countDangPai(GuipaiDangPai.dangType) < 2) {
				hongzhongAnke = true;
				break;
			}
		}
		taishu.setHongzhongAnke(hongzhongAnke);
		taishu.setHongzhongGang(shoupaixingWuguanJiesuancanshu.isGangchuHongzhong());
		taishu.setHongzhongPeng(shoupaixingWuguanJiesuancanshu.isPengchuHongzhong());
		taishu.setHunyiseHu(hu && shoupaixingWuguanJiesuancanshu.isHunyise());
		int shoupaiKeziCount = shoupaiPaiXing.countKezi();
		int shoupaiGangziCount = shoupaiPaiXing.countGangzi();
		ShoupaiDuiziZu huPaiDuiziZu = shoupaiPaiXing.findDuiziZuHasLastActionPai();
		boolean shunziWithTwoDang = shoupaiPaiXing.hasShunziWithDangTimes(GuipaiDangPai.dangType, 2);// 两张财神不能算顺子
		shoupaiPaiXing.getShunziList();
		boolean allShunzi = (shoupaixingWuguanJiesuancanshu.getPengchupaiZuCount() == 0
				&& shoupaixingWuguanJiesuancanshu.getGangchupaiZuCount() == 0 && shoupaiKeziCount == 0
				&& shoupaiGangziCount == 0 && (!shunziWithTwoDang));
		boolean biandangHu = false;
		boolean qiandangHu = false;
		if (hu && allShunzi) {
			ShoupaiDuiziZu shoupaiDuiziZu = shoupaiPaiXing.getDuiziList().get(0);
			boolean hongzhongDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.hongzhong);
			boolean facaiDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.facai);
			boolean menFengPaiDuizi = shoupaiDuiziZu.getDuiziType()
					.equals(shoupaixingWuguanJiesuancanshu.getMenFengPai());

			ShoupaiShunziZu huPaiShunziZu = shoupaiPaiXing.findShunziZuHasLastActionPai();
			if (huPaiShunziZu != null) {
				if (!huPaiShunziZu.getPai2().isLastActionPai()) {
					if (huPaiShunziZu.getPai3().isLastActionPai()) {
						MajiangPai zuoyongPaiType = huPaiShunziZu.getPai3().getZuoyongPaiType();
						if (!(zuoyongPaiType.equals(MajiangPai.sanwan) || zuoyongPaiType.equals(MajiangPai.santong)
								|| zuoyongPaiType.equals(MajiangPai.santiao))) {
							if (huPaiDuiziZu == null && !hongzhongDuizi && !facaiDuizi && !menFengPaiDuizi) {
								taishu.setPingHu(true);
							}
						} else {
							biandangHu = true;
						}
					} else if (huPaiShunziZu.getPai1().isLastActionPai()) {
						MajiangPai zuoyongPaiType = huPaiShunziZu.getPai1().getZuoyongPaiType();
						if (!(zuoyongPaiType.equals(MajiangPai.qiwan) || zuoyongPaiType.equals(MajiangPai.qitong)
								|| zuoyongPaiType.equals(MajiangPai.qitiao))) {
							if (huPaiDuiziZu == null && !hongzhongDuizi && !facaiDuizi && !menFengPaiDuizi) {
								taishu.setPingHu(true);
							}
						} else {
							biandangHu = true;
						}
					} else {
					}
				} else {
					qiandangHu = true;
				}
			}
		} else if (hu) {
			ShoupaiShunziZu huPaiShunziZu = shoupaiPaiXing.findShunziZuHasLastActionPai();
			if (huPaiShunziZu != null) {
				if (!huPaiShunziZu.getPai2().isLastActionPai()) {
					if (huPaiShunziZu.getPai3().isLastActionPai()) {
						biandangHu = true;
					} else if (huPaiShunziZu.getPai1().isLastActionPai()) {
						biandangHu = true;
					} else {
					}
				} else {
					qiandangHu = true;
				}
			}
		}
		taishu.setQianggangHu(qianggangHu);
		taishu.setQingyiseHu(hu && shoupaixingWuguanJiesuancanshu.isQingyise());
		taishu.setSancaishenHu(shoupaixingWuguanJiesuancanshu.getCaishenShu() == 3);
		taishu.setShuangCaishengHu(hu && shoupaixingWuguanJiesuancanshu.getCaishenShu() == 2);
		taishu.setSifengqiHu(couldSiFengQi);
		taishu.setTianHu(couldTianhu);

		boolean zuofengAnke = false;
		List<ShoupaiKeziZu> allKeziZuForMenFeng = shoupaiPaiXing
				.findAllKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
		for (ShoupaiKeziZu shoupaiKeziZu : allKeziZuForMenFeng) {
			if (shoupaiKeziZu.countDangPai(GuipaiDangPai.dangType) < 2) {
				zuofengAnke = true;
				break;
			}
		}

		taishu.setZuofengAnke(zuofengAnke);
		taishu.setZuofengGang(shoupaixingWuguanJiesuancanshu.isZuofengGang());
		taishu.setZuofengPeng(shoupaixingWuguanJiesuancanshu.isZuofengPeng());

		hushu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		hushu.setBiandangHu(biandangHu);
		hushu.setDandiaoHu(huPaiDuiziZu != null);

		hushu.setErbaangangShu(shoupaixingWuguanJiesuancanshu.getErbaangangCount());

		int erbaankeShu = 0;
		int erbapengShu = shoupaixingWuguanJiesuancanshu.getErbapengShu();
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getErbapaiArray().length; j++) {
			List<ShoupaiKeziZu> shoupaiKeziZuList = shoupaiPaiXing
					.findAllKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getErbapaiArray()[j]);
			if (!shoupaiKeziZuList.isEmpty()) {
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiKeziZuList) {
					if (dianpao) {
						if (shoupaiKeziZu.containsLastActionPai()) {
							erbapengShu++;
						} else {
							erbaankeShu++;
						}
					} else {
						erbaankeShu++;
					}
				}
			}
		}
		hushu.setErbaankeShu(erbaankeShu);
		hushu.setErbapengShu(erbapengShu);

		hushu.setErbaminggangShu(shoupaixingWuguanJiesuancanshu.getErbaminggangShu());
		hushu.setFacaiDuizi(shoupaiPaiXing.hasDuiziForPaiTypeNoCaishen(MajiangPai.facai));

		hushu.setFengziangangShu(shoupaixingWuguanJiesuancanshu.getFengziangangCount());

		int fengziankeShu = 0;
		int fengzipengShu = shoupaixingWuguanJiesuancanshu.getFengzipengShu();
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getFengzipaiArray().length; j++) {
			List<ShoupaiKeziZu> shoupaiKeziZuList = shoupaiPaiXing
					.findAllKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getFengzipaiArray()[j]);
			if (!shoupaiKeziZuList.isEmpty()) {
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiKeziZuList) {
					if (dianpao) {
						if (shoupaiKeziZu.containsLastActionPai()) {
							fengzipengShu++;
						} else {
							fengziankeShu++;
						}
					} else {
						fengziankeShu++;
					}
				}
			}
		}
		hushu.setFengziankeShu(fengziankeShu);
		hushu.setFengzipengShu(fengzipengShu);

		hushu.setFengziminggangShu(shoupaixingWuguanJiesuancanshu.getFengziminggangShu());
		hushu.setHongzhongDuizi(shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.hongzhong));
		hushu.setHu(hu);
		hushu.setQiandangHu(qiandangHu);

		hushu.setYijiuangangShu(shoupaixingWuguanJiesuancanshu.getYijiuangangCount());
		int yijiuankeShu = 0;
		int yijiupengShu = shoupaixingWuguanJiesuancanshu.getYijiupengShu();
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getYijiupaiArray().length; j++) {
			List<ShoupaiKeziZu> shoupaiKeziZuList = shoupaiPaiXing
					.findAllKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getYijiupaiArray()[j]);
			if (!shoupaiKeziZuList.isEmpty()) {
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiKeziZuList) {
					if (dianpao) {
						if (shoupaiKeziZu.containsLastActionPai()) {
							yijiupengShu++;
						} else {
							yijiuankeShu++;
						}
					} else {
						yijiuankeShu++;
					}
				}
			}
		}
		hushu.setYijiuankeShu(yijiuankeShu);
		hushu.setYijiupengShu(yijiupengShu);

		hushu.setYijiuminggangShu(shoupaixingWuguanJiesuancanshu.getYijiuminggangShu());
		hushu.setZimoHu(zimoHu);
		hushu.setZuofengDuizi(shoupaiPaiXing.hasDuiziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai()));

		hushu.calculate();
		return hushu;
	}

	// 其实点炮,抢杠胡,也包含自摸的意思，也调用这个
	private static List<ShoupaiPaiXing> calculateZimoHuPaiShoupaiPaiXingList(List<MajiangPai> guipaiList,
			boolean baibanIsGuipai, ShoupaiCalculator shoupaiCalculator, MajiangPlayer player,
			GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		if (!guipaiList.isEmpty()) {// 有财神
			return calculateHuPaiShoupaiPaiXingListWithCaishen(guipaiList, baibanIsGuipai, shoupaiCalculator, player,
					gouXingPanHu, huPai);
		} else {// 没财神
			return calculateHuPaiShoupaiPaiXingListWithoutCaishen(shoupaiCalculator, player, gouXingPanHu, huPai);
		}
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingList(List<MajiangPai> guipaiList,
			boolean baibanIsGuipai, ShoupaiCalculator shoupaiCalculator) {
		if (!guipaiList.isEmpty()) {// 有财神
			return calculateBuhuShoupaiPaiXingListWithCaishen(guipaiList, baibanIsGuipai, shoupaiCalculator);
		} else {// 没财神
			return calculateBuhuShoupaiPaiXingListWithoutCaishen(shoupaiCalculator);
		}
	}

	private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithoutCaishen(
			ShoupaiCalculator shoupaiCalculator, MajiangPlayer player, GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算构型
		List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
		int chichuShunziCount = player.countChichupaiZu();
		int pengchuKeziCount = player.countPengchupaiZu();
		int gangchuGangziCount = player.countGangchupaiZu();
		for (GouXing gouXing : gouXingList) {
			boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
					gangchuGangziCount);
			if (hu) {
				// 计算牌型
				List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
				for (PaiXing paiXing : paiXingList) {
					ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
					// 对ShoupaiPaiXing还要变换最后弄进的牌
					List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
							.differentiateShoupaiPaiXingByLastActionPai(huPai);
					huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
				}
			}
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingListWithoutCaishen(
			ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiPaiXing> buhuShoupaiPaiXingList = new ArrayList<>();
		// 计算构型
		List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
		for (GouXing gouXing : gouXingList) {
			// 计算牌型
			List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
			for (PaiXing paiXing : paiXingList) {
				ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
				buhuShoupaiPaiXingList.add(shoupaiPaiXing);
			}
		}
		return buhuShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateHuPaiShoupaiPaiXingListWithCaishen(List<MajiangPai> guipaiList,
			boolean baibanIsGuipai, ShoupaiCalculator shoupaiCalculator, MajiangPlayer player,
			GouXingPanHu gouXingPanHu, MajiangPai huPai) {
		int chichuShunziCount = player.countChichupaiZu();
		int pengchuKeziCount = player.countPengchupaiZu();
		int gangchuGangziCount = player.countGangchupaiZu();
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		MajiangPai[] paiTypesForGuipaiAct = calculatePaiTypesForGuipaiAct(baibanIsGuipai);// 鬼牌可以扮演的牌类
		// 开始循环财神各种当法，算构型
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = calculateShoupaiWithGuipaiDangGouXingZuList(
				guipaiList, paiTypesForGuipaiAct, shoupaiCalculator);
		// 对于可胡的构型，计算出所有牌型
		for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
			GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
			List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
			for (GouXing gouXing : gouXingList) {
				boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
						gangchuGangziCount);
				if (hu) {
					// 先把所有当的鬼牌加入计算器
					for (int i = 0; i < guipaiDangPaiArray.length; i++) {
						shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
					}
					// 计算牌型
					huPaiShoupaiPaiXingList.addAll(calculateAllShoupaiPaiXingForGouXingWithHupai(gouXing,
							shoupaiCalculator, guipaiDangPaiArray, huPai));
					// 再把所有当的鬼牌移出计算器
					for (int i = 0; i < guipaiDangPaiArray.length; i++) {
						shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
					}
				}

			}
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateBuhuShoupaiPaiXingListWithCaishen(List<MajiangPai> guipaiList,
			boolean baibanIsGuipai, ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		MajiangPai[] paiTypesForGuipaiAct = calculatePaiTypesForGuipaiAct(baibanIsGuipai);// 鬼牌可以扮演的牌类
		// 开始循环财神各种当法，算构型
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = calculateShoupaiWithGuipaiDangGouXingZuList(
				guipaiList, paiTypesForGuipaiAct, shoupaiCalculator);
		// 对构型计算出所有牌型
		for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
			GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
			List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
			for (GouXing gouXing : gouXingList) {
				// 先把所有当的鬼牌加入计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
				}
				// 计算牌型
				huPaiShoupaiPaiXingList.addAll(calculateAllShoupaiPaiXingForGouXingWithoutHupai(gouXing,
						shoupaiCalculator, guipaiDangPaiArray));
				// 再把所有当的鬼牌移出计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
				}
			}
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateAllShoupaiPaiXingForGouXingWithHupai(GouXing gouXing,
			ShoupaiCalculator shoupaiCalculator, GuipaiDangPai[] guipaiDangPaiArray, MajiangPai huPai) {
		boolean sancaishen = (guipaiDangPaiArray.length == 3);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
			}

			// 对于每一个ShoupaiPaiXing还要变换最后弄进的牌
			for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
				List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
						.differentiateShoupaiPaiXingByLastActionPai(huPai);
				huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
			}

		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiPaiXing> calculateAllShoupaiPaiXingForGouXingWithoutHupai(GouXing gouXing,
			ShoupaiCalculator shoupaiCalculator, GuipaiDangPai[] guipaiDangPaiArray) {
		boolean sancaishen = (guipaiDangPaiArray.length == 3);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipaiDangQitapai() > (sancaishen ? 2 : 1)) {
						i.remove();
						break;
					}
				}
			}
			huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingList);
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiWithGuipaiDangGouXingZu> calculateShoupaiWithGuipaiDangGouXingZuList(// TODO
																									// 鬼牌当可以抽象到majiang.dml
			List<MajiangPai> guipaiList, MajiangPai[] paiTypesForGuipaiAct, ShoupaiCalculator shoupaiCalculator) {
		List<ShoupaiWithGuipaiDangGouXingZu> shoupaiWithGuipaiDangGouXingZuList = new ArrayList<>();

		// 两个鬼牌只要套两层循环分别遍历所有的当法，三个鬼牌套三层循环，更多个鬼牌以此类推。
		// 出于通用考虑，这样写死几层循环的算法实现起来代码会很长，很不合理。
		// 这里改用一个面向通用的n个鬼牌的算法：
		// n个鬼牌，每个鬼牌都有不同的变化，这样组合起来最终就有很多种组合法。
		// 比如第一个鬼牌当一筒，第二个鬼牌当六万是一种。第一个鬼牌当九条，第二个鬼牌当南风又是一种。所以(假设总共一万种组合)可以给每种组合编号0,1,2,3,4,......,9998,9999
		// 我们的思路是，不管几个鬼牌，这种编号是扁平的（不需要考虑套几层循环），只是最大编号不同而已。最大编号问题就是总共几种组合法的问题。
		// 解总共几种组合法的问题非常简单，假设一个鬼牌有14种当法（扮演14种不同的牌），那两个鬼牌就是14*14种组合法，三个就是14*14*14种，n个就是14的n次方种。
		// 所以我们可以一趟循环走组合编号。那么现在唯一可以利用的就是编号值本身，我们需要从编号值推断出具体的组合方案。
		// 我们考虑人工罗列这些方案是怎么做的。假设两个鬼牌，可以扮演一万到九万。那第一个鬼牌先取一万，第二个鬼牌从一万开始一个个按顺序取过来取到九万，
		// 接着就是第一个鬼牌取二万，第二个鬼牌再次从一万开始一个个按顺序取过来取到九万，然后第一个鬼牌取三万......
		// 我们想想这不就是进位翻牌器吗？翻牌器也就是一个计数器，能覆盖0到n的所有数字。
		// 翻牌器的原理其实就是10进制。所以我们利用进制来实现 从编号值推断出具体的组合方案。
		// 我们还是来看下10进制的情况，假设三个鬼牌，一个组合编码，也就是一个数字,x,那他的百位的数值可以用来代表第一个鬼牌的当法，十位的数值可以用来代表第二个鬼牌的当法，
		// 个位的数值可以用来代表第三个鬼牌的当法。
		// 比如编码123,那就意味着 第一个鬼牌当二万，第二个鬼牌当三万，第三个鬼牌当四万。所以现在的问题是从一个数字中取出它百位的数值，十位的数值和个位的数值。
		// 我们来解这个问题，要知道123的百位数值也就是要知道123里面有几个100（这个100是事先算好的模），所以123除以100得到的商是1，这个1就是结果了，
		// 然后余数是23，这个23不要丢掉，这个余数去除以10得到的商恰好就是十位的值，2，个位以此类推......
		// 当然麻将他不是10进制的，不管几进制，可以证明此算法是通用的。

		int guipaiCount = guipaiList.size();
		int maxZuheCode = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount);
		int[] modArray = new int[guipaiCount];
		for (int i = 0; i < guipaiCount; i++) {
			modArray[i] = (int) Math.pow(paiTypesForGuipaiAct.length, guipaiCount - 1 - i);
		}
		for (int zuheCode = 0; zuheCode < maxZuheCode; zuheCode++) {
			GuipaiDangPai[] guipaiDangPaiArray = new GuipaiDangPai[guipaiCount];
			int temp = zuheCode;
			int previousGuipaiDangIdx = 0;
			for (int i = 0; i < guipaiCount; i++) {
				int mod = modArray[i];
				int shang = temp / mod;
				if (shang >= previousGuipaiDangIdx) {
					int yu = temp % mod;
					guipaiDangPaiArray[i] = new GuipaiDangPai(guipaiList.get(i), paiTypesForGuipaiAct[shang]);
					temp = yu;
					previousGuipaiDangIdx = shang;
				} else {
					guipaiDangPaiArray = null;
					break;
				}
			}
			if (guipaiDangPaiArray != null) {
				// 先把所有当的鬼牌加入计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
				}
				// 计算构型
				List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
				// 再把所有当的鬼牌移出计算器
				for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
				}
				ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu = new ShoupaiWithGuipaiDangGouXingZu();
				shoupaiWithGuipaiDangGouXingZu.setGouXingList(gouXingList);
				shoupaiWithGuipaiDangGouXingZu.setGuipaiDangPaiArray(guipaiDangPaiArray);
				shoupaiWithGuipaiDangGouXingZuList.add(shoupaiWithGuipaiDangGouXingZu);
			}
		}
		return shoupaiWithGuipaiDangGouXingZuList;
	}

	private static MajiangPai[] calculatePaiTypesForGuipaiAct(boolean baibanIsGuipai) {
		MajiangPai[] xushupaiArray = MajiangPai.xushupaiArray();
		MajiangPai[] fengpaiArray = MajiangPai.fengpaiArray();
		MajiangPai[] paiTypesForGuipaiAct;
		if (baibanIsGuipai) {// 白板是鬼牌
			paiTypesForGuipaiAct = new MajiangPai[xushupaiArray.length + fengpaiArray.length + 1];
			System.arraycopy(xushupaiArray, 0, paiTypesForGuipaiAct, 0, xushupaiArray.length);
			System.arraycopy(fengpaiArray, 0, paiTypesForGuipaiAct, xushupaiArray.length, fengpaiArray.length);
			paiTypesForGuipaiAct[31] = MajiangPai.facai;
		} else {// 白板不是鬼牌
			paiTypesForGuipaiAct = new MajiangPai[xushupaiArray.length + fengpaiArray.length + 2];
			System.arraycopy(xushupaiArray, 0, paiTypesForGuipaiAct, 0, xushupaiArray.length);
			System.arraycopy(fengpaiArray, 0, paiTypesForGuipaiAct, xushupaiArray.length, fengpaiArray.length);
			paiTypesForGuipaiAct[31] = MajiangPai.hongzhong;
			paiTypesForGuipaiAct[32] = MajiangPai.facai;
		}
		return paiTypesForGuipaiAct;
	}

}
