package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.GouXing;
import com.dml.majiang.GouXingPanHu;
import com.dml.majiang.GuipaiDangPai;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionUpdater;
import com.dml.majiang.PaiXing;
import com.dml.majiang.Pan;
import com.dml.majiang.QuanPaiXing;
import com.dml.majiang.ShoupaiCalculator;
import com.dml.majiang.ShoupaiPaiXing;
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
		List<QuanPaiXing> quanPaiXingList = new ArrayList<>();
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
								for (ShoupaiPaiXing finalShoupaiPaiXing : shoupaiPaiXingListWithDifftentLastActionPaiInZu) {
									QuanPaiXing quanPaiXing = new QuanPaiXing(finalShoupaiPaiXing,
											player.getChichupaiZuList(), player.getPengchupaiZuList(),
											player.getGangchupaiZuList());
									quanPaiXingList.add(quanPaiXing);
								}

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
						for (ShoupaiPaiXing finalShoupaiPaiXing : shoupaiPaiXingListWithDifftentLastActionPaiInZu) {
							QuanPaiXing quanPaiXing = new QuanPaiXing(finalShoupaiPaiXing, player.getChichupaiZuList(),
									player.getPengchupaiZuList(), player.getGangchupaiZuList());
							quanPaiXingList.add(quanPaiXing);
						}
					}
				}
			}

		}
		if (!quanPaiXingList.isEmpty()) {// 有胡牌型
			// 要选出分数最高的胡牌型
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
