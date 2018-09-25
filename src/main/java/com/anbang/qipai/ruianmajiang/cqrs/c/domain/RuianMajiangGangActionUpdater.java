package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.GangType;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.gang.MajiangPlayerGangActionUpdater;
import com.dml.majiang.player.action.hu.MajiangHuAction;
import com.dml.majiang.player.action.mo.GanghouBupai;
import com.dml.majiang.player.action.mo.MajiangMoAction;
import com.dml.majiang.player.shoupai.gouxing.GouXingPanHu;

/**
 * 别人可以抢杠胡。原先碰牌后自己摸到碰出刻子牌的第四张牌而形成的明杠,才可以抢
 * 
 * @author Neo
 *
 */
public class RuianMajiangGangActionUpdater implements MajiangPlayerGangActionUpdater {

	@Override
	public void updateActions(MajiangGangAction gangAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		currentPan.clearAllPlayersActionCandidates();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());
		boolean baibanIsGuipai = currentPan.getPublicGuipaiSet().contains(MajiangPai.baiban);

		// 看看是不是有其他玩家可以抢杠胡
		boolean qiangganghu = false;
		if (gangAction.getGangType().equals(GangType.kezigangmo)
				|| gangAction.getGangType().equals(GangType.kezigangshoupai)) {
			RuianMajiangPanResultBuilder ruianMajiangPanResultBuilder = (RuianMajiangPanResultBuilder) ju
					.getCurrentPanResultBuilder();
			int dihu = ruianMajiangPanResultBuilder.getDihu();
			boolean dapao = ruianMajiangPanResultBuilder.isDapao();
			GouXingPanHu gouXingPanHu = ju.getGouXingPanHu();
			MajiangPlayer currentPlayer = player;
			while (true) {
				MajiangPlayer xiajia = currentPan.findXiajia(currentPlayer);
				if (xiajia.getId().equals(player.getId())) {
					break;
				}
                final SiFengQiMoDaActionListener siFengQiMoDaActionListener = ju.getActionStatisticsListenerManager().findListener(SiFengQiMoDaActionListener.class);
                final boolean couldSiFengQi = siFengQiMoDaActionListener.couldSiFengQi(gangAction.getActionPlayerId());
				RuianMajiangHu bestHu = RuianMajiangJiesuanCalculator.calculateBestQianggangHu(couldSiFengQi,gangAction.getPai(),
						dapao, dihu, gouXingPanHu, xiajia, baibanIsGuipai);
				if (bestHu != null) {
					bestHu.setQianggang(true);
					xiajia.addActionCandidate(new MajiangHuAction(xiajia.getId(), bestHu));
					xiajia.checkAndGenerateGuoCandidateAction();
					qiangganghu = true;
					break;
				}

				currentPlayer = xiajia;
			}
		}

		// 没有抢杠胡，杠完之后要摸牌
		if (!qiangganghu) {
			player.addActionCandidate(new MajiangMoAction(player.getId(),
					new GanghouBupai(gangAction.getPai(), gangAction.getGangType())));
		}
	}

}
