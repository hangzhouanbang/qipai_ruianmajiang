package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerScore;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.majiang.ChichuPaiZu;
import com.dml.majiang.MajiangChiAction;
import com.dml.majiang.MajiangGangAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPengAction;
import com.dml.majiang.MajiangPlayerAction;
import com.dml.majiang.MajiangPlayerActionType;
import com.dml.majiang.Shunzi;

public class RuianMajiangPanPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private boolean hu;
	private List<List<ResultShoupaiVO>> resultShoupaiZuList = new ArrayList<>();
	private List<List<ResultChupaiVO>> resultChupaiZuList = new ArrayList<>();

	/**
	 * 这个是打了几炮
	 */
	private int pao;

	/**
	 * 这个是非结算的胡数
	 */
	private int hushu;

	/**
	 * 这个是结算分
	 */
	private int score;

	public RuianMajiangPanPlayerResultVO(GamePlayerDbo gamePlayerDbo, RuianMajiangPanPlayerResult panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		headimgurl = gamePlayerDbo.getHeadimgurl();
		hu = panPlayerResult.isHu();
		RuianMajiangPanPlayerScore ruianMajiangPanPlayerScore = panPlayerResult.getScore();
		pao = ruianMajiangPanPlayerScore.getPao().getValue();
		hushu = ruianMajiangPanPlayerScore.getHushu().getValue();
		score = ruianMajiangPanPlayerScore.getJiesuanScore();

		MajiangPlayerAction chipenggangAction = panPlayerResult.getChipenggangAction();
		MajiangPai hupaiChijinPai = null;
		Shunzi hupaiShunzi = null;
		MajiangPai hupaiPengjinPai = null;
		MajiangPai hupaiGangjinPai = null;
		if (chipenggangAction != null) {
			if (chipenggangAction.getType().equals(MajiangPlayerActionType.chi)) {
				MajiangChiAction chiAction = (MajiangChiAction) chipenggangAction;
				hupaiChijinPai = chiAction.getChijinPai();
				hupaiShunzi = chiAction.getShunzi();
			} else if (chipenggangAction.getType().equals(MajiangPlayerActionType.peng)) {
				MajiangPengAction pengAction = (MajiangPengAction) chipenggangAction;
				hupaiPengjinPai = pengAction.getPai();
			} else if (chipenggangAction.getType().equals(MajiangPlayerActionType.gang)) {
				MajiangGangAction gangAction = (MajiangGangAction) chipenggangAction;
				hupaiGangjinPai = gangAction.getPai();
			} else {
			}
		}

		List<ChichuPaiZu> chichuPaiZuList = panPlayerResult.getChichupaiZuList();
		// 多顺子匹配只做一次
		boolean foundShunzi = false;
		for (ChichuPaiZu chichuPaiZu : chichuPaiZuList) {
			List<ResultChupaiVO> chupaiList = new ArrayList<>();
			if (hupaiShunzi != null && !foundShunzi) {
				Shunzi chichuShunzi = chichuPaiZu.getShunzi();
				if (hupaiShunzi.equals(chichuShunzi)) {
					MajiangPai pai1 = chichuShunzi.getPai1();
					if (pai1.equals(hupaiChijinPai)) {
						chupaiList.add(new ResultChupaiVO(pai1, true));
					} else {
						chupaiList.add(new ResultChupaiVO(pai1, false));
					}
					MajiangPai pai2 = chichuShunzi.getPai2();
					if (pai2.equals(hupaiChijinPai)) {
						chupaiList.add(new ResultChupaiVO(pai2, true));
					} else {
						chupaiList.add(new ResultChupaiVO(pai2, false));
					}
					MajiangPai pai3 = chichuShunzi.getPai3();
					if (pai3.equals(hupaiChijinPai)) {
						chupaiList.add(new ResultChupaiVO(pai3, true));
					} else {
						chupaiList.add(new ResultChupaiVO(pai3, false));
					}
					foundShunzi = true;
				}
			}
		}
		if (hu) {

		} else {

		}
	}

	public String getPlayerId() {
		return playerId;
	}

	public String getNickname() {
		return nickname;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public boolean isHu() {
		return hu;
	}

	public List<List<ResultShoupaiVO>> getResultShoupaiZuList() {
		return resultShoupaiZuList;
	}

	public List<List<ResultChupaiVO>> getResultChupaiZuList() {
		return resultChupaiZuList;
	}

	public int getPao() {
		return pao;
	}

	public int getHushu() {
		return hushu;
	}

	public int getScore() {
		return score;
	}

}
