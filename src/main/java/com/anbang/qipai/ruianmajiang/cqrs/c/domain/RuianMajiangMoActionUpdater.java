package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.GuipaiDangPai;
import com.dml.majiang.Ju;
import com.dml.majiang.MajiangMoAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPlayer;
import com.dml.majiang.MajiangPlayerMoActionUpdater;
import com.dml.majiang.Pan;
import com.dml.majiang.ShoupaiCalculator;

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
		// 先判断成不成胡
		ShoupaiCalculator shoupaiCalculator = player.getShoupaiCalculator();
		List<MajiangPai> guipaiList = player.findGuipaiList();
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
					// for (int i = 0; i < guipaiDangPaiArray.length; i++) {
					// shoupaiCalculator.addPai(guipaiDangPaiArray[i].getDangpai());
					// }
					// List<GouXing> gouXingList = shoupaiCalculator.calculateAllGouXing();
				}
			}

			// ShoupaiWithGuipaiDangGouXingZu

		} else {// 没财神

		}

		// // 非胡牌型特殊胡-三财神
		// MoGuipaiCounter moGuipaiCounter =
		// ju.getActionStatisticsListenerManager().findListener(MoGuipaiCounter.class);
		// if (moGuipaiCounter.getCount() == 3) {
		//
		// }

		// 可胡牌型的胡

		// 需要有“过”
		player.checkAndGenerateGuoCandidateAction();

		// TODO 啥也不能干，那只能打出牌
		player.generateDaActions();

	}

}
