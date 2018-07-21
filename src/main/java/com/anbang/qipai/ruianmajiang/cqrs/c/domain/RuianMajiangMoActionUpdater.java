package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.GanghouBupai;
import com.dml.majiang.GouXing;
import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.GuipaiDangPai;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionUpdater;
import com.dml.majiang.MopaiReason;
import com.dml.majiang.PaiXing;
import com.dml.majiang.Pan;
import com.dml.majiang.ShoupaiCalculator;
import com.dml.majiang.ShoupaiDuiziZu;
import com.dml.majiang.ShoupaiPaiXing;
import com.dml.majiang.ShoupaiShunziZu;
import com.dml.majiang.ShoupaiWithGuipaiDangGouXingZu;

public class RuianMajiangMoActionUpdater implements MajiangPlayerMoActionUpdater {

	@Override
	public void updateActions(MajiangMoAction moAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(moAction.getActionPlayerId());
		player.clearActionCandidates();

		// 摸到公开牌了要补牌(继续摸牌)
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);
		MajiangPai gangmoShoupai = player.getGangmoShoupai();
		if (baibanIsGuipai) {// 白板是鬼牌
			if (gangmoShoupai.equals(MajiangPai.hongzhong)) {// 红中是公开牌
				player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
				return;
			}
		} else {// 白板不是鬼牌
			if (gangmoShoupai.equals(MajiangPai.baiban)) { // 白板是公开牌
				player.addActionCandidate(new MajiangMoAction(player.getId(), new RuianBupai()));
				return;
			}
		}

		// 有手牌或刻子可以杠这个摸来的牌
		player.tryShoupaigangmoAndGenerateCandidateAction();
		player.tryKezigangmoAndGenerateCandidateAction();

		// 杠四个手牌
		player.tryGangsigeshoupaiAndGenerateCandidateAction();

		// 胡
		GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();
		List<ShoupaiPaiXing> huPaiShoupaiPaiXingList = new ArrayList<>();
		if (!guipaiList.isEmpty()) {// 有财神
			MajiangPai[] xushupaiArray = MajiangPai.xushupaiArray();
			MajiangPai[] paiTypesForGuipaiAct;// 鬼牌可以扮演的牌类
			if (baibanIsGuipai) {// 白板是鬼牌
				paiTypesForGuipaiAct = new MajiangPai[xushupaiArray.length + 1];
				System.arraycopy(xushupaiArray, 0, paiTypesForGuipaiAct, 0, xushupaiArray.length);
				paiTypesForGuipaiAct[27] = MajiangPai.facai;
			} else {// 白板不是鬼牌
				paiTypesForGuipaiAct = new MajiangPai[xushupaiArray.length + 2];
				System.arraycopy(xushupaiArray, 0, paiTypesForGuipaiAct, 0, xushupaiArray.length);
				paiTypesForGuipaiAct[27] = MajiangPai.hongzhong;
				paiTypesForGuipaiAct[28] = MajiangPai.facai;
			}

			// 开始循环财神各种当法，算构型
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
					// 把刚摸来的牌(如果不是鬼牌)加入计算器
					if (!player.gangmoGuipai()) {
						shoupaiCalculator.addPai(gangmoShoupai);
					}
					// 计算构型
					List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
					// 再把所有当的鬼牌移出计算器
					for (int i = 0; i < guipaiDangPaiArray.length; i++) {
						shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
					}
					// 把刚摸来的牌(如果不是鬼牌)移出计算器
					if (!player.gangmoGuipai()) {
						shoupaiCalculator.removePai(gangmoShoupai);
					}
					ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu = new ShoupaiWithGuipaiDangGouXingZu();
					shoupaiWithGuipaiDangGouXingZu.setGouXingList(gouXingList);
					shoupaiWithGuipaiDangGouXingZu.setGuipaiDangPaiArray(guipaiDangPaiArray);
					shoupaiWithGuipaiDangGouXingZuList.add(shoupaiWithGuipaiDangGouXingZu);
				}
			}

			// 对于可胡的构型，计算出所有牌型
			for (ShoupaiWithGuipaiDangGouXingZu shoupaiWithGuipaiDangGouXingZu : shoupaiWithGuipaiDangGouXingZuList) {
				GuipaiDangPai[] guipaiDangPaiArray = shoupaiWithGuipaiDangGouXingZu.getGuipaiDangPaiArray();
				List<GouXing> gouXingList = shoupaiWithGuipaiDangGouXingZu.getGouXingList();
				for (GouXing gouXing : gouXingList) {
					int chichuShunziCount = player.countChichupaiZu();
					int pengchuKeziCount = player.countPengchupaiZu();
					int gangchuGangziCount = player.countGangchupaiZu();
					boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
							gangchuGangziCount);
					if (hu) {
						// 先把所有当的鬼牌加入计算器
						for (int i = 0; i < guipaiDangPaiArray.length; i++) {
							shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
						}
						// 把刚摸来的牌(如果不是鬼牌)加入计算器
						if (!player.gangmoGuipai()) {
							shoupaiCalculator.addPai(gangmoShoupai);
						}
						// 计算牌型
						List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
						// 再把所有当的鬼牌移出计算器
						for (int i = 0; i < guipaiDangPaiArray.length; i++) {
							shoupaiCalculator.removePai(guipaiDangPaiArray[i].getDangpai());
						}
						// 把刚摸来的牌(如果不是鬼牌)移出计算器
						if (!player.gangmoGuipai()) {
							shoupaiCalculator.removePai(gangmoShoupai);
						}
						for (PaiXing paiXing : paiXingList) {
							List<ShoupaiPaiXing> shoupaiPaiXingList = paiXing
									.generateShoupaiPaiXingByGuipaiDangPai(guipaiDangPaiArray);
							// 对于每一个ShoupaiPaiXing还要变换最后弄进的牌
							for (ShoupaiPaiXing shoupaiPaiXing : shoupaiPaiXingList) {
								List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
										.differentiateShoupaiPaiXingByLastActionPai(gangmoShoupai);
								huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
							}

						}
					}
				}
			}

		} else {// 没财神
			// 把刚摸来的牌(如果不是鬼牌)加入计算器
			if (!player.gangmoGuipai()) {
				shoupaiCalculator.addPai(gangmoShoupai);
			}
			// 计算构型
			List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
			// 把刚摸来的牌(如果不是鬼牌)移出计算器
			if (!player.gangmoGuipai()) {
				shoupaiCalculator.removePai(gangmoShoupai);
			}

			for (GouXing gouXing : gouXingList) {
				int chichuShunziCount = player.countChichupaiZu();
				int pengchuKeziCount = player.countPengchupaiZu();
				int gangchuGangziCount = player.countGangchupaiZu();
				boolean hu = gouXingPanHu.panHu(gouXing.getGouXingCode(), chichuShunziCount, pengchuKeziCount,
						gangchuGangziCount);
				if (hu) {
					// 把刚摸来的牌(如果不是鬼牌)加入计算器
					if (!player.gangmoGuipai()) {
						shoupaiCalculator.addPai(gangmoShoupai);
					}
					// 计算牌型
					List<PaiXing> paiXingList = shoupaiCalculator.calculateAllPaiXingFromGouXing(gouXing);
					// 把刚摸来的牌(如果不是鬼牌)移出计算器
					if (!player.gangmoGuipai()) {
						shoupaiCalculator.removePai(gangmoShoupai);
					}
					for (PaiXing paiXing : paiXingList) {
						ShoupaiPaiXing shoupaiPaiXing = paiXing.generateAllBenPaiShoupaiPaiXing();
						// 对ShoupaiPaiXing还要变换最后弄进的牌
						List<ShoupaiPaiXing> shoupaiPaiXingListWithDifftentLastActionPaiInZu = shoupaiPaiXing
								.differentiateShoupaiPaiXingByLastActionPai(gangmoShoupai);
						huPaiShoupaiPaiXingList.addAll(shoupaiPaiXingListWithDifftentLastActionPaiInZu);
					}
				}
			}

		}
		if (!huPaiShoupaiPaiXingList.isEmpty()) {// 有胡牌型
			// 要选出分数最高的胡牌型
			// 先计算和手牌型无关的参数
			boolean pengchuHongzhong = player.ifPengchuForPaiType(MajiangPai.hongzhong);
			boolean gangchuHongzhong = player.ifGangchuForPaiType(MajiangPai.hongzhong);
			boolean pengchuFacai = player.ifPengchuForPaiType(MajiangPai.facai);
			boolean gangchuFacai = player.ifGangchuForPaiType(MajiangPai.facai);
			MajiangPai menFengPai = player.fengpaiForMenfeng();
			boolean zuofengPeng = player.ifPengchuForPaiType(menFengPai);
			boolean zuofengGang = player.ifGangchuForPaiType(menFengPai);
			int baibanShu = player.countPublicPai();
			int caishenShu = player.countGuipai();
			boolean allXushupaiInSameCategory = player.allXushupaiInSameCategory();
			boolean hasZipai = player.hasZipai();
			boolean qingyise = (allXushupaiInSameCategory && !hasZipai);
			boolean hunyise = (allXushupaiInSameCategory && hasZipai);

			int yijiupengShu = 0;
			MajiangPai[] yijiupaiArray = new MajiangPai[] { MajiangPai.yiwan, MajiangPai.jiuwan, MajiangPai.yitong,
					MajiangPai.jiutong, MajiangPai.yitiao, MajiangPai.jiutiao };
			for (int i = 0; i < yijiupaiArray.length; i++) {
				if (player.ifPengchuForPaiType(yijiupaiArray[i])) {
					yijiupengShu++;
				}
			}
			int erbapengShu = 0;
			MajiangPai[] erbapaiArray = new MajiangPai[] { MajiangPai.erwan, MajiangPai.sanwan, MajiangPai.siwan,
					MajiangPai.wuwan, MajiangPai.liuwan, MajiangPai.qiwan, MajiangPai.bawan,

					MajiangPai.ertong, MajiangPai.santong, MajiangPai.sitong, MajiangPai.wutong, MajiangPai.liutong,
					MajiangPai.qitong, MajiangPai.batong,

					MajiangPai.ertiao, MajiangPai.santiao, MajiangPai.sitiao, MajiangPai.wutiao, MajiangPai.liutiao,
					MajiangPai.qitiao, MajiangPai.batiao };
			for (int i = 0; i < erbapaiArray.length; i++) {
				if (player.ifPengchuForPaiType(erbapaiArray[i])) {
					erbapengShu++;
				}
			}
			int fengzipengShu = 0;
			MajiangPai[] fengzipaiArray = new MajiangPai[] { MajiangPai.dongfeng, MajiangPai.nanfeng, MajiangPai.xifeng,
					MajiangPai.beifeng };
			for (int i = 0; i < fengzipaiArray.length; i++) {
				if (player.ifPengchuForPaiType(fengzipaiArray[i])) {
					fengzipengShu++;
				}
			}
			int yijiuminggangShu = 0;
			for (int i = 0; i < yijiupaiArray.length; i++) {
				if (player.ifGangchuForPaiType(yijiupaiArray[i])) {
					yijiuminggangShu++;
				}
			}
			int erbaminggangShu = 0;
			for (int i = 0; i < erbapaiArray.length; i++) {
				if (player.ifGangchuForPaiType(erbapaiArray[i])) {
					erbaminggangShu++;
				}
			}
			int fengziminggangShu = 0;
			for (int i = 0; i < fengzipaiArray.length; i++) {
				if (player.ifGangchuForPaiType(fengzipaiArray[i])) {
					fengziminggangShu++;
				}
			}
			int fangruShoupaiCount = player.countFangruShoupai();
			int chichupaiZuCount = player.countChichupaiZu();
			MopaiReason mopaiReason = moAction.getReason();
			int pengchupaiZuCount = player.countPengchupaiZu();
			int gangchupaiZuCount = player.countGangchupaiZu();
			// 开始计算和手牌型有关的参数
			RuianMajiangPanPlayerScore[] scoreArray = new RuianMajiangPanPlayerScore[huPaiShoupaiPaiXingList.size()];
			int[] scoreValueScoreArrayIdxArray = new int[scoreArray.length];// 低16位scoreArray的idx,高16位分数值
			for (int i = 0; i < scoreArray.length; i++) {
				ShoupaiPaiXing huPaiShoupaiPaiXing = huPaiShoupaiPaiXingList.get(i);

				RuianMajiangPanPlayerScore score = new RuianMajiangPanPlayerScore();
				RuianMajiangPao pao = new RuianMajiangPao();
				RuianMajiangHushu hushu = new RuianMajiangHushu();
				RuianMajiangTaishu taishu = new RuianMajiangTaishu();
				hushu.setTaishu(taishu);
				score.setHushu(hushu);
				score.setPao(pao);
				scoreArray[i] = score;

				taishu.setBaibanShu(baibanShu);
				taishu.setDanzhangdiaoHu(fangruShoupaiCount == 1);
				taishu.setDiHu(false);// TODO 地胡
				int shoupaiShunziCount = huPaiShoupaiPaiXing.countShunzi();
				taishu.setDuiduiHu(chichupaiZuCount == 0 && shoupaiShunziCount == 0);
				boolean facaiAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(MajiangPai.facai);
				taishu.setFacaiAnke(facaiAnke);
				boolean facaiAngang = huPaiShoupaiPaiXing.hasGangziForPaiType(MajiangPai.facai);
				taishu.setFacaiGang(gangchuFacai | facaiAngang);
				taishu.setFacaiPeng(pengchuFacai);
				taishu.setGangkaiHu(mopaiReason.getName().equals(GanghouBupai.name));
				boolean hongzhongAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(MajiangPai.hongzhong);
				taishu.setHongzhongAnke(hongzhongAnke);
				boolean hongzhongGang = huPaiShoupaiPaiXing.hasGangziForPaiType(MajiangPai.hongzhong);
				taishu.setHongzhongGang(gangchuHongzhong | hongzhongGang);
				taishu.setHongzhongPeng(pengchuHongzhong);
				taishu.setHunyiseHu(hunyise);
				int shoupaiKeziCount = huPaiShoupaiPaiXing.countKezi();
				int shoupaiGangziCount = huPaiShoupaiPaiXing.countGangzi();
				boolean allShunzi = (pengchupaiZuCount == 0 && gangchupaiZuCount == 0 && shoupaiKeziCount == 0
						&& shoupaiGangziCount == 0);
				boolean biandangHu = false;
				if (allShunzi) {
					ShoupaiDuiziZu shoupaiDuiziZu = huPaiShoupaiPaiXing.getDuiziList().get(0);
					boolean hongzhongDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.hongzhong);
					boolean facaiDuizi = shoupaiDuiziZu.getDuiziType().equals(MajiangPai.facai);
					boolean menFengPaiDuizi = shoupaiDuiziZu.getDuiziType().equals(menFengPai);
					if (!hongzhongDuizi && !facaiDuizi && !menFengPaiDuizi) {
						ShoupaiDuiziZu huPaiDuiziZu = huPaiShoupaiPaiXing.findDuiziZuHasLastActionPai();
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
								}
							}
						}
					}
				}
				taishu.setQianggangHu(false);// TODO 抢杠胡
				taishu.setQingyiseHu(qingyise);
				taishu.setSancaishenHu(caishenShu == 3);
				taishu.setShuangCaisheng(caishenShu == 2);
				taishu.setSifengqiHu(false);// TODO 用统计器来做
				taishu.setTianHu(false);// TODO 天胡
				boolean zuofengAnke = huPaiShoupaiPaiXing.hasKeziForPaiType(menFengPai);
				taishu.setZuofengAnke(zuofengAnke);
				boolean zuofengAnGang = huPaiShoupaiPaiXing.hasGangziForPaiType(menFengPai);
				taishu.setZuofengGang(zuofengGang | zuofengAnGang);
				taishu.setZuofengPeng(zuofengPeng);

				hushu.setBaibanShu(baibanShu);
				hushu.setBiandangHu(biandangHu);
				hushu.setDandiaoHu(dandiaoHu);

			}

		}

		// // 非胡牌型特殊胡-三财神
		// MoGuipaiCounter moGuipaiCounter =
		// ju.getActionStatisticsListenerManager().findListener(MoGuipaiCounter.class);
		// if (moGuipaiCounter.getCount() == 3) {
		//
		// }

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// TODO 啥也不能干，那只能打出牌
		player.generateDaActions();

	}

}
