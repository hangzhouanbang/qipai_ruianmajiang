package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerScore;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.majiang.ChichuPaiZu;
import com.dml.majiang.GangchuPaiZu;
import com.dml.majiang.MajiangChiAction;
import com.dml.majiang.MajiangGangAction;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.MajiangPengAction;
import com.dml.majiang.MajiangPlayerAction;
import com.dml.majiang.MajiangPlayerActionType;
import com.dml.majiang.PengchuPaiZu;
import com.dml.majiang.ShoupaiGangziZu;
import com.dml.majiang.ShoupaiKeziZu;
import com.dml.majiang.ShoupaiPaiXing;
import com.dml.majiang.ShoupaiShunziZu;
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
			resultChupaiZuList.add(chupaiList);
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

		List<PengchuPaiZu> pengchupaiZuList = panPlayerResult.getPengchupaiZuList();
		for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
			List<ResultChupaiVO> chupaiList = new ArrayList<>();
			resultChupaiZuList.add(chupaiList);
			MajiangPai pengchuKeziPai = pengchuPaiZu.getKezi().getPaiType();
			if (hupaiPengjinPai.equals(pengchuKeziPai)) {
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, true));
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, false));
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, false));
			} else {
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, false));
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, false));
				chupaiList.add(new ResultChupaiVO(pengchuKeziPai, false));
			}
		}

		List<GangchuPaiZu> gangchupaiZuList = panPlayerResult.getGangchupaiZuList();
		for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
			List<ResultChupaiVO> chupaiList = new ArrayList<>();
			resultChupaiZuList.add(chupaiList);
			MajiangPai gangchuGangziPai = gangchuPaiZu.getGangzi().getPaiType();
			if (hupaiGangjinPai.equals(gangchuGangziPai)) {
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, true));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
			} else {
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
				chupaiList.add(new ResultChupaiVO(gangchuGangziPai, false));
			}
		}

		if (hu) {
			ShoupaiPaiXing shoupaiPaiXing = panPlayerResult.getBestShoupaiPaiXing();
			List<ShoupaiShunziZu> shunziList = shoupaiPaiXing.getShunziList();
			for (ShoupaiShunziZu shoupaiShunziZu : shunziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiShunziZu.getPai3()));
			}

			List<ShoupaiKeziZu> keziList = shoupaiPaiXing.getKeziList();
			for (ShoupaiKeziZu shoupaiKeziZu : keziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiKeziZu.getPai3()));
			}

			List<ShoupaiGangziZu> gangziList = shoupaiPaiXing.getGangziList();
			for (ShoupaiGangziZu shoupaiGangziZu : gangziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai2()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai3()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiGangziZu.getPai4()));
			}
		} else {
			List<MajiangPai> shoupaiList = panPlayerResult.getShoupaiList();
			Set<MajiangPai> guipaiTypeSet = panPlayerResult.getGuipaiTypeSet();
			List<ResultShoupaiVO> list = new ArrayList<>();
			resultShoupaiZuList.add(list);
			for (MajiangPai pai : shoupaiList) {
				if (guipaiTypeSet.contains(pai)) {
					list.add(new ResultShoupaiVO(pai, true));
				} else {
					list.add(new ResultShoupaiVO(pai, false));
				}
			}
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
