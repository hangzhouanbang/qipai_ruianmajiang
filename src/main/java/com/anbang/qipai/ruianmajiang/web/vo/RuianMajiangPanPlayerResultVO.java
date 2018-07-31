package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerScore;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.majiang.ChichuPaiZu;
import com.dml.majiang.GangchuPaiZu;
import com.dml.majiang.MajiangPai;
import com.dml.majiang.PengchuPaiZu;
import com.dml.majiang.ShoupaiDuiziZu;
import com.dml.majiang.ShoupaiGangziZu;
import com.dml.majiang.ShoupaiKeziZu;
import com.dml.majiang.ShoupaiPaiXing;
import com.dml.majiang.ShoupaiShunziZu;
import com.dml.majiang.Shunzi;

public class RuianMajiangPanPlayerResultVO {

	private String playerId;
	private String nickname;
	private String headimgurl;
	private boolean zhuang;
	private boolean hu;
	private boolean zimo;
	private boolean dianpao;
	private List<MajiangPai> publicPaiList;
	private List<MajiangPai> caishenList;
	private List<List<ResultShoupaiVO>> resultShoupaiZuList = new ArrayList<>();
	private List<Shunzi> shunziList = new ArrayList<>();
	private List<MajiangPai> keziTypeList = new ArrayList<>();
	private List<GangchuPaiZuVO> gangchuList = new ArrayList<>();

	/**
	 * 这个是打了几炮
	 */
	private int pao;

	/**
	 * 这个是非结算的胡数
	 */
	private int hushu;

	private RuianMajiangTaishuVO taishu;

	/**
	 * 这个是结算分
	 */
	private int score;

	public RuianMajiangPanPlayerResultVO(GamePlayerDbo gamePlayerDbo, String zhuangPlayerId, boolean zimo,
			String dianpaoPlayerId, RuianMajiangPanPlayerResult panPlayerResult) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		headimgurl = gamePlayerDbo.getHeadimgurl();
		if (playerId.equals(zhuangPlayerId)) {
			zhuang = true;
		}
		hu = panPlayerResult.isHu();
		publicPaiList = new ArrayList<>(panPlayerResult.getPublicPaiList());
		RuianMajiangPanPlayerScore ruianMajiangPanPlayerScore = panPlayerResult.getScore();
		RuianMajiangPao ruianMajiangPao = ruianMajiangPanPlayerScore.getPao();
		if (ruianMajiangPao != null) {
			pao = ruianMajiangPao.getValue();
		}
		hushu = ruianMajiangPanPlayerScore.getHushu().getValue();
		taishu = new RuianMajiangTaishuVO(ruianMajiangPanPlayerScore.getHushu().getTaishu());
		score = ruianMajiangPanPlayerScore.getJiesuanScore();

		List<ChichuPaiZu> chichuPaiZuList = panPlayerResult.getChichupaiZuList();
		for (ChichuPaiZu chichuPaiZu : chichuPaiZuList) {
			shunziList.add(chichuPaiZu.getShunzi());
		}

		List<PengchuPaiZu> pengchupaiZuList = panPlayerResult.getPengchupaiZuList();
		for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
			keziTypeList.add(pengchuPaiZu.getKezi().getPaiType());
		}

		List<GangchuPaiZu> gangchupaiZuList = panPlayerResult.getGangchupaiZuList();
		for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
			gangchuList.add(new GangchuPaiZuVO(gangchuPaiZu));
		}

		if (hu) {
			this.zimo = zimo;
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

			List<ShoupaiDuiziZu> duiziList = shoupaiPaiXing.getDuiziList();
			for (ShoupaiDuiziZu shoupaiDuiziZu : duiziList) {
				List<ResultShoupaiVO> shoupaiList = new ArrayList<>();
				resultShoupaiZuList.add(shoupaiList);
				shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai1()));
				shoupaiList.add(new ResultShoupaiVO(shoupaiDuiziZu.getPai2()));
			}

		} else {
			if (!zimo) {
				if (playerId.equals(dianpaoPlayerId)) {
					dianpao = true;
				}
			}
			List<MajiangPai> shoupaiList = panPlayerResult.getShoupaiList();
			Set<MajiangPai> guipaiTypeSet = panPlayerResult.getGuipaiTypeSet();
			caishenList = new ArrayList<>();
			List<ResultShoupaiVO> list = new ArrayList<>();
			resultShoupaiZuList.add(list);
			for (MajiangPai pai : shoupaiList) {
				if (guipaiTypeSet.contains(pai)) {
					caishenList.add(pai);
				} else {
					list.add(new ResultShoupaiVO(pai));
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

	public boolean isZhuang() {
		return zhuang;
	}

	public boolean isHu() {
		return hu;
	}

	public boolean isZimo() {
		return zimo;
	}

	public boolean isDianpao() {
		return dianpao;
	}

	public List<MajiangPai> getPublicPaiList() {
		return publicPaiList;
	}

	public List<MajiangPai> getCaishenList() {
		return caishenList;
	}

	public List<List<ResultShoupaiVO>> getResultShoupaiZuList() {
		return resultShoupaiZuList;
	}

	public List<Shunzi> getShunziList() {
		return shunziList;
	}

	public List<MajiangPai> getKeziTypeList() {
		return keziTypeList;
	}

	public List<GangchuPaiZuVO> getGangchuList() {
		return gangchuList;
	}

	public int getPao() {
		return pao;
	}

	public int getHushu() {
		return hushu;
	}

	public RuianMajiangTaishuVO getTaishu() {
		return taishu;
	}

	public int getScore() {
		return score;
	}

}
