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
	public static RuianMajiangHu calculateBestZimoHu(boolean couldTianhu, boolean dapao, int dihu,
			GouXingPanHu gouXingPanHu, MajiangPlayer player, MajiangMoAction moAction, boolean baibanIsGuipai) {
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
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(false, couldTianhu, false,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true,
						moAction.getReason().getName().equals(GanghouBupai.name), true, false, dihu, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	// 抢杠胡
	public static RuianMajiangHu calculateBestQianggangHu(MajiangPai gangPai, boolean dapao, int dihu,
			GouXingPanHu gouXingPanHu, MajiangPlayer player, boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做
		shoupaiCalculator.addPai(gangPai);
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, player.getGangmoShoupai());
		shoupaiCalculator.removePai(gangPai);
		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(true, false, false,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true, false, true, true, dihu, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	// 点炮胡
	public static RuianMajiangHu calculateBestDianpaoHu(boolean couldDihu, boolean dapao, int dihu,
			GouXingPanHu gouXingPanHu, MajiangPlayer player, boolean baibanIsGuipai, MajiangPai hupai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, hupai);

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			// 要选出分数最高的牌型
			// 先计算和手牌型无关的参数
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
			RuianMajiangPanPlayerScore bestScore = null;
			ShoupaiPaiXing bestHuShoupaiPaiXing = null;
			for (ShoupaiPaiXing shoupaiPaiXing : huPaiShoupaiPaiXingList) {
				RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(true, false, couldDihu,
						shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, true, false, false, false, dihu, dapao);
				if (bestScore == null || bestScore.getValue() < score.getValue()) {
					bestScore = score;
					bestHuShoupaiPaiXing = shoupaiPaiXing;
				}
			}
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestScore);
		} else {// 不成胡
			return null;
		}
	}

	public static RuianMajiangPanPlayerScore calculateBestScoreForBuhuPlayer(boolean dapao, int dihu,
			MajiangPlayer player, boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> shoupaiPaiXingList = calculateBuhuShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator);

		// 要选出分数最高的牌型
		// 先计算和手牌型无关的参数
		ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
		RuianMajiangPanPlayerScore bestScore = null;
		for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
			RuianMajiangPanPlayerScore score = calculateScoreForShoupaiPaiXing(false, false, false,
					shoupaixingWuguanJiesuancanshu, shoupaiPaiXing, false, false, false, false, dihu, dapao);
			if (bestScore == null || bestScore.getValue() < score.getValue()) {
				bestScore = score;
			}
		}
		return bestScore;
	}

	private static RuianMajiangPanPlayerScore calculateScoreForShoupaiPaiXing(boolean dianpao, boolean couldTianhu,
			boolean couldDihu, ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu,
			ShoupaiPaiXing shoupaiPaiXing, boolean hu, boolean gangkaiHu, boolean zimoHu, boolean qianggangHu, int dihu,
			boolean dapao) {
		RuianMajiangPanPlayerScore score = new RuianMajiangPanPlayerScore();
		RuianMajiangHushu hushu = calculateHushu(dianpao, couldTianhu, couldDihu, hu, gangkaiHu, zimoHu, qianggangHu,
				dihu, shoupaixingWuguanJiesuancanshu, shoupaiPaiXing);
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
		boolean facaiAnke = shoupaiPaiXing.hasKeziForPaiType(MajiangPai.facai);
		pao.setFacaiAnke(facaiAnke);
		pao.setFacaiGang(shoupaixingWuguanJiesuancanshu.isGangchuFacai());
		pao.setFacaiPeng(shoupaixingWuguanJiesuancanshu.isPengchuFacai());
		boolean hongzhongAnke = shoupaiPaiXing.hasKeziForPaiType(MajiangPai.hongzhong);
		if (hongzhongAnke) {
			ShoupaiKeziZu hongzhongKeziZu = shoupaiPaiXing.findFirstKeziZuForPaiType(MajiangPai.hongzhong);
			if (hongzhongKeziZu.countGuipai() == 0) {
				pao.setHongzhongAnke(true);
			}
		}

		pao.setHongzhongGang(shoupaixingWuguanJiesuancanshu.isGangchuHongzhong());
		pao.setHongzhongPeng(shoupaixingWuguanJiesuancanshu.isPengchuHongzhong());
		pao.setHu(hu);

		boolean zuofengAnke = shoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
		if (zuofengAnke) {
			ShoupaiKeziZu zuofengKeziZu = shoupaiPaiXing
					.findFirstKeziZuForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
			if (zuofengKeziZu.countGuipai() == 0) {
				pao.setZuofengAnke(true);
			}
		}

		pao.setZuofengGang(shoupaixingWuguanJiesuancanshu.isZuofengGang());
		pao.setZuofengPeng(shoupaixingWuguanJiesuancanshu.isZuofengPeng());

		pao.calculate();

		return pao;
	}

	private static RuianMajiangHushu calculateHushu(boolean dianpao, boolean couldTianhu, boolean couldDihu, boolean hu,
			boolean gangkaiHu, boolean zimoHu, boolean qianggangHu, int dihu,
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu, ShoupaiPaiXing shoupaiPaiXing) {
		RuianMajiangHushu hushu = new RuianMajiangHushu();
		hushu.setDihu(dihu);
		RuianMajiangTaishu taishu = new RuianMajiangTaishu();
		hushu.setTaishu(taishu);
		taishu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		taishu.setDanzhangdiaoHu(hu && shoupaixingWuguanJiesuancanshu.getFangruShoupaiCount() == 1);
		taishu.setDiHu(couldDihu);
		int shoupaiShunziCount = shoupaiPaiXing.countShunzi();
		if (hu) {
			taishu.setDuiduiHu(shoupaixingWuguanJiesuancanshu.getChichupaiZuCount() == 0 && shoupaiShunziCount == 0);
		}
		boolean facaiAnke = shoupaiPaiXing.hasKeziForPaiType(MajiangPai.facai);
		taishu.setFacaiAnke(facaiAnke);
		taishu.setFacaiGang(shoupaixingWuguanJiesuancanshu.isGangchuFacai());
		taishu.setFacaiPeng(shoupaixingWuguanJiesuancanshu.isPengchuFacai());
		taishu.setGangkaiHu(gangkaiHu);
		boolean hongzhongAnke = shoupaiPaiXing.hasKeziForPaiType(MajiangPai.hongzhong);
		taishu.setHongzhongAnke(hongzhongAnke);
		taishu.setHongzhongGang(shoupaixingWuguanJiesuancanshu.isGangchuHongzhong());
		taishu.setHongzhongPeng(shoupaixingWuguanJiesuancanshu.isPengchuHongzhong());
		taishu.setHunyiseHu(hu && shoupaixingWuguanJiesuancanshu.isHunyise());
		int shoupaiKeziCount = shoupaiPaiXing.countKezi();
		int shoupaiGangziCount = shoupaiPaiXing.countGangzi();
		ShoupaiDuiziZu huPaiDuiziZu = shoupaiPaiXing.findDuiziZuHasLastActionPai();
		boolean allShunzi = (shoupaixingWuguanJiesuancanshu.getPengchupaiZuCount() == 0
				&& shoupaixingWuguanJiesuancanshu.getGangchupaiZuCount() == 0 && shoupaiKeziCount == 0
				&& shoupaiGangziCount == 0);
		boolean biandangHu = false;
		boolean qiandangHu = false;
		if (hu && allShunzi) {
			ShoupaiDuiziZu shoupaiDuiziZu = shoupaiPaiXing.getDuiziList().get(0);
			boolean hongzhongDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.hongzhong);
			boolean facaiDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.facai);
			boolean menFengPaiDuizi = shoupaiDuiziZu.getDuiziType()
					.equals(shoupaixingWuguanJiesuancanshu.getMenFengPai());
			if (!hongzhongDuizi && !facaiDuizi && !menFengPaiDuizi) {
				if (huPaiDuiziZu == null) {
					ShoupaiShunziZu huPaiShunziZu = shoupaiPaiXing.findShunziZuHasLastActionPai();
					if (huPaiShunziZu != null) {
						if (!huPaiShunziZu.getPai2().isLastActionPai()) {
							if (huPaiShunziZu.getPai3().isLastActionPai()) {
								MajiangPai zuoyongPaiType = huPaiShunziZu.getPai3().getZuoyongPaiType();
								if (!(zuoyongPaiType.equals(MajiangPai.sanwan)
										|| zuoyongPaiType.equals(MajiangPai.santong)
										|| zuoyongPaiType.equals(MajiangPai.santiao))) {
									taishu.setPingHu(true);
								} else {
									biandangHu = true;
								}
							} else if (huPaiShunziZu.getPai1().isLastActionPai()) {
								MajiangPai zuoyongPaiType = huPaiShunziZu.getPai1().getZuoyongPaiType();
								if (!(zuoyongPaiType.equals(MajiangPai.qiwan)
										|| zuoyongPaiType.equals(MajiangPai.qitong)
										|| zuoyongPaiType.equals(MajiangPai.qitiao))) {
									taishu.setPingHu(true);
								} else {
									biandangHu = true;
								}
							} else {
							}
						} else {
							qiandangHu = true;
						}
					}
				}
			}
		}
		taishu.setQianggangHu(qianggangHu);
		taishu.setQingyiseHu(hu && shoupaixingWuguanJiesuancanshu.isQingyise());
		taishu.setSancaishenHu(shoupaixingWuguanJiesuancanshu.getCaishenShu() == 3);
		taishu.setShuangCaishengHu(hu && shoupaixingWuguanJiesuancanshu.getCaishenShu() == 2);
		taishu.setSifengqiHu(false);// TODO 用统计器来做
		taishu.setTianHu(couldTianhu);
		boolean zuofengAnke = shoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
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
		hushu.setFacaiDuizi(shoupaiPaiXing.hasDuiziForPaiType(MajiangPai.facai));

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
