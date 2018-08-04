package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.Arrays;
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
	public static RuianMajiangHu calculateBestZimoHu(int dihu, GouXingPanHu gouXingPanHu, MajiangPlayer player,
			MajiangMoAction moAction, boolean baibanIsGuipai) {
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

			RuianMajiangHushu[] hushuArray = new RuianMajiangHushu[huPaiShoupaiPaiXingList.size()];
			// 要选出分数最高的胡牌型
			int bestHushuShoupaiPaiXingIdx = calculateBestHushuShoupaiPaiXingIdx(player, huPaiShoupaiPaiXingList,
					hushuArray, true, moAction.getReason().getName().equals(GanghouBupai.name), true, dihu);
			// 找到了最佳
			ShoupaiPaiXing bestHuShoupaiPaiXing = huPaiShoupaiPaiXingList.get(bestHushuShoupaiPaiXingIdx);
			RuianMajiangHushu bestHuHushu = hushuArray[bestHushuShoupaiPaiXingIdx];
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestHuHushu);
		} else {// 不成胡
			return null;
		}
	}

	// 点炮胡
	public static RuianMajiangHu calculateBestDianpaoHu(int dihu, GouXingPanHu gouXingPanHu, MajiangPlayer player,
			boolean baibanIsGuipai, MajiangPai hupai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = calculateZimoHuPaiShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator, player, gouXingPanHu, hupai);

		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型

			RuianMajiangHushu[] hushuArray = new RuianMajiangHushu[huPaiShoupaiPaiXingList.size()];
			// 要选出分数最高的胡牌型
			int bestHushuShoupaiPaiXingIdx = calculateBestHushuShoupaiPaiXingIdx(player, huPaiShoupaiPaiXingList,
					hushuArray, true, false, false, dihu);
			// 找到了最佳
			ShoupaiPaiXing bestHuShoupaiPaiXing = huPaiShoupaiPaiXingList.get(bestHushuShoupaiPaiXingIdx);
			RuianMajiangHushu bestHuHushu = hushuArray[bestHushuShoupaiPaiXingIdx];
			return new RuianMajiangHu(bestHuShoupaiPaiXing, bestHuHushu);
		} else {// 不成胡
			return null;
		}
	}

	public static RuianMajiangHushu calculateBestHushuForBuhuPlayer(int dihu, MajiangPlayer player,
			boolean baibanIsGuipai) {
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();// TODO 也可以用统计器做

		List<ShoupaiPaiXing> shoupaiPaiXingList = calculateBuhuShoupaiPaiXingList(guipaiList, baibanIsGuipai,
				shoupaiCalculator);

		RuianMajiangHushu[] hushuArray = new RuianMajiangHushu[shoupaiPaiXingList.size()];
		// 要选出胡数最高的牌型
		int bestHushuShoupaiPaiXingIdx = calculateBestHushuShoupaiPaiXingIdx(player, shoupaiPaiXingList, hushuArray,
				false, false, false, dihu);
		// 找到了最佳
		RuianMajiangHushu bestHushu = hushuArray[bestHushuShoupaiPaiXingIdx];
		return bestHushu;
	}

	private static int calculateBestHushuShoupaiPaiXingIdx(MajiangPlayer player,
			List<ShoupaiPaiXing> huPaiShoupaiPaiXingList, RuianMajiangHushu[] hushuArray, boolean hu, boolean gangkaiHu,
			boolean zimoHu, int dihu) {
		// 要选出胡数最高的胡牌型
		// 先计算和手牌型无关的参数
		ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu = new ShoupaixingWuguanJiesuancanshu(player);
		// 开始计算和手牌型有关的参数
		int[] hushuArrayValueIdxArray = new int[hushuArray.length];// 低16位scoreArray的idx,高16位分数值
		for (int i = 0; i < hushuArray.length; i++) {
			ShoupaiPaiXing huPaiShoupaiPaiXing = huPaiShoupaiPaiXingList.get(i);
			RuianMajiangHushu hushu = calculateHushu(hu, gangkaiHu, zimoHu, dihu, shoupaixingWuguanJiesuancanshu,
					huPaiShoupaiPaiXing);
			// 牌型变化和炮无关，所以不用考虑炮
			hushuArray[i] = hushu;
			hushuArrayValueIdxArray[i] = (i | (hushu.getValue() << 16));// 低16位scoreArray的idx,高16位分数值
		}
		Arrays.sort(hushuArrayValueIdxArray);
		return ((hushuArrayValueIdxArray[hushuArrayValueIdxArray.length - 1] << 16) >>> 16);
	}

	private static RuianMajiangHushu calculateHushu(boolean hu, boolean gangkaiHu, boolean zimoHu, int dihu,
			ShoupaixingWuguanJiesuancanshu shoupaixingWuguanJiesuancanshu, ShoupaiPaiXing huPaiShoupaiPaiXing) {
		RuianMajiangHushu hushu = new RuianMajiangHushu();
		hushu.setDihu(dihu);
		RuianMajiangTaishu taishu = new RuianMajiangTaishu();
		hushu.setTaishu(taishu);
		taishu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		taishu.setDanzhangdiaoHu(shoupaixingWuguanJiesuancanshu.getFangruShoupaiCount() == 1);
		taishu.setDiHu(false);// TODO 地胡
		int shoupaiShunziCount = huPaiShoupaiPaiXing.countShunzi();
		if (hu) {
			taishu.setDuiduiHu(shoupaixingWuguanJiesuancanshu.getChichupaiZuCount() == 0 && shoupaiShunziCount == 0);
		}
		boolean facaiAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(MajiangPai.facai);
		taishu.setFacaiAnke(facaiAnke);
		boolean facaiAngang = huPaiShoupaiPaiXing.hasGangziForPaiType(MajiangPai.facai);
		taishu.setFacaiGang(shoupaixingWuguanJiesuancanshu.isGangchuFacai() | facaiAngang);
		taishu.setFacaiPeng(shoupaixingWuguanJiesuancanshu.isPengchuFacai());
		taishu.setGangkaiHu(gangkaiHu);
		boolean hongzhongAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(MajiangPai.hongzhong);
		taishu.setHongzhongAnke(hongzhongAnke);
		boolean hongzhongGang = huPaiShoupaiPaiXing.hasGangziForPaiType(MajiangPai.hongzhong);
		taishu.setHongzhongGang(shoupaixingWuguanJiesuancanshu.isGangchuHongzhong() | hongzhongGang);
		taishu.setHongzhongPeng(shoupaixingWuguanJiesuancanshu.isPengchuHongzhong());
		taishu.setHunyiseHu(shoupaixingWuguanJiesuancanshu.isHunyise());
		int shoupaiKeziCount = huPaiShoupaiPaiXing.countKezi();
		int shoupaiGangziCount = huPaiShoupaiPaiXing.countGangzi();
		ShoupaiDuiziZu huPaiDuiziZu = null;
		boolean allShunzi = (shoupaixingWuguanJiesuancanshu.getPengchupaiZuCount() == 0
				&& shoupaixingWuguanJiesuancanshu.getGangchupaiZuCount() == 0 && shoupaiKeziCount == 0
				&& shoupaiGangziCount == 0);
		boolean biandangHu = false;
		boolean qiandangHu = false;
		if (hu && allShunzi) {
			ShoupaiDuiziZu shoupaiDuiziZu = huPaiShoupaiPaiXing.getDuiziList().get(0);
			boolean hongzhongDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.hongzhong);
			boolean facaiDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.facai);
			boolean menFengPaiDuizi = shoupaiDuiziZu.getDuiziType()
					.equals(shoupaixingWuguanJiesuancanshu.getMenFengPai());
			if (!hongzhongDuizi && !facaiDuizi && !menFengPaiDuizi) {
				huPaiDuiziZu = huPaiShoupaiPaiXing.findDuiziZuHasLastActionPai();
				if (huPaiDuiziZu == null) {
					ShoupaiShunziZu huPaiShunziZu = huPaiShoupaiPaiXing.findShunziZuHasLastActionPai();
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
		taishu.setQianggangHu(false);// TODO 抢杠胡
		taishu.setQingyiseHu(shoupaixingWuguanJiesuancanshu.isQingyise());
		taishu.setSancaishenHu(shoupaixingWuguanJiesuancanshu.getCaishenShu() == 3);
		taishu.setShuangCaisheng(shoupaixingWuguanJiesuancanshu.getCaishenShu() == 2);
		taishu.setSifengqiHu(false);// TODO 用统计器来做
		taishu.setTianHu(false);// TODO 天胡
		boolean zuofengAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
		taishu.setZuofengAnke(zuofengAnke);
		boolean zuofengAnGang = huPaiShoupaiPaiXing.hasGangziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai());
		taishu.setZuofengGang(shoupaixingWuguanJiesuancanshu.isZuofengGang() | zuofengAnGang);
		taishu.setZuofengPeng(shoupaixingWuguanJiesuancanshu.isZuofengPeng());

		hushu.setBaibanShu(shoupaixingWuguanJiesuancanshu.getBaibanShu());
		hushu.setBiandangHu(biandangHu);
		hushu.setDandiaoHu(huPaiDuiziZu != null);

		int erbaangangShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getErbapaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasGangziForPaiType(shoupaixingWuguanJiesuancanshu.getErbapaiArray()[j])) {
				erbaangangShu++;
			}
		}
		hushu.setErbaangangShu(erbaangangShu);

		int erbaankeShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getErbapaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getErbapaiArray()[j])) {
				erbaankeShu++;
			}
		}
		hushu.setErbaankeShu(erbaankeShu);

		hushu.setErbaminggangShu(shoupaixingWuguanJiesuancanshu.getErbaminggangShu());
		hushu.setErbapengShu(shoupaixingWuguanJiesuancanshu.getErbapengShu());
		hushu.setFacaiDuizi(huPaiShoupaiPaiXing.hasDuiziForPaiType(MajiangPai.facai));

		int fengziangangShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getFengzipaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasGangziForPaiType(shoupaixingWuguanJiesuancanshu.getFengzipaiArray()[j])) {
				fengziangangShu++;
			}
		}
		hushu.setFengziangangShu(fengziangangShu);

		int fengziankeShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getFengzipaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getFengzipaiArray()[j])) {
				fengziankeShu++;
			}
		}
		hushu.setFengziankeShu(fengziankeShu);

		hushu.setFengziminggangShu(shoupaixingWuguanJiesuancanshu.getFengziminggangShu());
		hushu.setFengzipengShu(shoupaixingWuguanJiesuancanshu.getFengzipengShu());
		hushu.setHongzhongDuizi(huPaiShoupaiPaiXing.hasDuiziForPaiType(MajiangPai.hongzhong));
		hushu.setHu(hu);
		hushu.setQiandangHu(qiandangHu);

		int yijiuangangShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getYijiupaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasGangziForPaiType(shoupaixingWuguanJiesuancanshu.getYijiupaiArray()[j])) {
				yijiuangangShu++;
			}
		}
		hushu.setYijiuangangShu(yijiuangangShu);

		int yijiuankeShu = 0;
		for (int j = 0; j < shoupaixingWuguanJiesuancanshu.getYijiupaiArray().length; j++) {
			if (huPaiShoupaiPaiXing.hasKeziForPaiType(shoupaixingWuguanJiesuancanshu.getYijiupaiArray()[j])) {
				yijiuankeShu++;
			}
		}
		hushu.setYijiuankeShu(yijiuankeShu);

		hushu.setYijiuminggangShu(shoupaixingWuguanJiesuancanshu.getYijiuminggangShu());
		hushu.setYijiupengShu(shoupaixingWuguanJiesuancanshu.getYijiupengShu());
		hushu.setZimoHu(zimoHu);
		hushu.setZuofengDuizi(huPaiShoupaiPaiXing.hasDuiziForPaiType(shoupaixingWuguanJiesuancanshu.getMenFengPai()));

		// pao.setBaibanShu(baibanShu);
		// pao.setCaishenShu(caishenShu);
		// pao.setFacaiAnke(facaiAnke);
		// pao.setFacaiGang(gangchuFacai | facaiAngang);
		// pao.setFacaiPeng(pengchuFacai);
		// if (hongzhongAnke) {
		// ShoupaiKeziZu hongzhongKeziZu =
		// huPaiShoupaiPaiXing.findFirstKeziZuForPaiType(MajiangPai.hongzhong);
		// if (hongzhongKeziZu.countGuipai() == 0) {
		// pao.setHongzhongAnke(true);
		// }
		// }
		//
		// boolean hongzhongAnGang = false;
		// if (hongzhongGang) {
		// ShoupaiGangziZu hongzhongGangziZu = huPaiShoupaiPaiXing
		// .findFirstGangziZuForPaiType(MajiangPai.hongzhong);
		// if (hongzhongGangziZu.countGuipai() == 0) {
		// pao.setHongzhongAnke(true);
		// }
		// }
		//
		// pao.setHongzhongGang(gangchuHongzhong | hongzhongAnGang);
		// pao.setHongzhongPeng(pengchuHongzhong);
		// pao.setHu(true);
		//
		// if (zuofengAnke) {
		// ShoupaiKeziZu zuofengKeziZu =
		// huPaiShoupaiPaiXing.findFirstKeziZuForPaiType(menFengPai);
		// if (zuofengKeziZu.countGuipai() == 0) {
		// pao.setZuofengAnke(true);
		// }
		// }
		//
		// boolean zuofengAnGangNoCaishen = false;
		// if (zuofengAnGang) {
		// ShoupaiGangziZu zuofengGangziZu =
		// huPaiShoupaiPaiXing.findFirstGangziZuForPaiType(menFengPai);
		// if (zuofengGangziZu.countGuipai() == 0) {
		// zuofengAnGangNoCaishen = true;
		// }
		// }
		// pao.setZuofengGang(zuofengGang | zuofengAnGangNoCaishen);
		// pao.setZuofengPeng(zuofengPeng);

		hushu.calculate();
		return hushu;
	}

	// 其实点炮也包含自摸的意思，也调用这个
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
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByGuipaiDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipai() > 1) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipai() > 1) {
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
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		// 计算牌型
		List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
		for (PaiXing paiXing : paiXingList) {
			List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing.generateShoupaiPaiXingByGuipaiDangPai(guipaiDangPaiArray);
			// 过滤暗杠或暗刻有两个财神当的
			Iterator<ShoupaiPaiXing> i = shoupaiPaiXingList.iterator();
			while (i.hasNext()) {
				ShoupaiPaiXing shoupaiPaiXing = i.next();
				for (ShoupaiKeziZu shoupaiKeziZu : shoupaiPaiXing.getKeziList()) {
					if (shoupaiKeziZu.countGuipai() > 1) {
						i.remove();
						break;
					}
				}
				for (ShoupaiGangziZu shoupaiGangziZu : shoupaiPaiXing.getGangziList()) {
					if (shoupaiGangziZu.countGuipai() > 1) {
						i.remove();
						break;
					}
				}
			}
			huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingList);
		}
		return huPaiShoupaiPaiXingList;
	}

	private static List<ShoupaiWithGuipaiDangGouXingZu> calculateShoupaiWithGuipaiDangGouXingZuList(
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
