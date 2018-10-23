package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerScore;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.RuianMajiangPanPlayerResultDbo;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.fenzu.Shunzi;
import com.dml.majiang.player.chupaizu.ChichuPaiZu;
import com.dml.majiang.player.chupaizu.GangchuPaiZu;
import com.dml.majiang.player.chupaizu.PengchuPaiZu;
import com.dml.majiang.player.shoupai.ShoupaiDuiziZu;
import com.dml.majiang.player.shoupai.ShoupaiGangziZu;
import com.dml.majiang.player.shoupai.ShoupaiKeziZu;
import com.dml.majiang.player.shoupai.ShoupaiPaiXing;
import com.dml.majiang.player.shoupai.ShoupaiShunziZu;

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
	 * 这个是炮分
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

	public RuianMajiangPanPlayerResultVO(MajiangGamePlayerDbo gamePlayerDbo, String zhuangPlayerId, boolean zimo,
			String dianpaoPlayerId, RuianMajiangPanPlayerResultDbo panPlayerResultDbo) {
		playerId = gamePlayerDbo.getPlayerId();
		nickname = gamePlayerDbo.getNickname();
		headimgurl = gamePlayerDbo.getHeadimgurl();
		if (playerId.equals(zhuangPlayerId)) {
			zhuang = true;
		}
		hu = panPlayerResultDbo.getPlayer().getHu() != null;
		publicPaiList = new ArrayList<>(panPlayerResultDbo.getPlayer().getPublicPaiList());
		RuianMajiangPanPlayerScore ruianMajiangPanPlayerScore = panPlayerResultDbo.getPlayerResult().getScore();
		RuianMajiangPao ruianMajiangPao = ruianMajiangPanPlayerScore.getPao();
		if (ruianMajiangPao != null) {
			pao = ruianMajiangPao.getValue() * 5;
		}
		hushu = ruianMajiangPanPlayerScore.getHushu().quzhengValue();
		taishu = new RuianMajiangTaishuVO(ruianMajiangPanPlayerScore.getHushu().getTaishu());
		score = ruianMajiangPanPlayerScore.getJiesuanScore();

		List<ChichuPaiZu> chichuPaiZuList = panPlayerResultDbo.getPlayer().getChichupaiZuList();
		for (ChichuPaiZu chichuPaiZu : chichuPaiZuList) {
			shunziList.add(chichuPaiZu.getShunzi());
		}

		List<PengchuPaiZu> pengchupaiZuList = panPlayerResultDbo.getPlayer().getPengchupaiZuList();
		for (PengchuPaiZu pengchuPaiZu : pengchupaiZuList) {
			keziTypeList.add(pengchuPaiZu.getKezi().getPaiType());
		}

		List<GangchuPaiZu> gangchupaiZuList = panPlayerResultDbo.getPlayer().getGangchupaiZuList();
		for (GangchuPaiZu gangchuPaiZu : gangchupaiZuList) {
			gangchuList.add(new GangchuPaiZuVO(gangchuPaiZu));
		}

		if (hu) {
			this.zimo = zimo;
			ShoupaiPaiXing shoupaiPaiXing = panPlayerResultDbo.getPlayer().getHu().getShoupaiPaiXing();
			if (shoupaiPaiXing == null) {// 三财神胡没有牌型
				caishenList = new ArrayList<>(panPlayerResultDbo.getPlayer().getFangruGuipaiList());
				List<MajiangPai> shoupaiList = panPlayerResultDbo.getPlayer().getFangruShoupaiList();
				List<ResultShoupaiVO> list = new ArrayList<>();
				resultShoupaiZuList.add(list);
				for (MajiangPai pai : shoupaiList) {
					list.add(new ResultShoupaiVO(pai));
				}
				MajiangPai gangmoShoupai = panPlayerResultDbo.getPlayer().getGangmoShoupai().getPai();
				ResultShoupaiVO lastPai = new ResultShoupaiVO(gangmoShoupai);
				lastPai.setHupai(true);
				if (caishenList.contains(gangmoShoupai)) {
					lastPai.setCaishen(true);
				}
				list.add(lastPai);
			} else {
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
			}
		} else {
			if (!zimo) {
				if (playerId.equals(dianpaoPlayerId)) {
					dianpao = true;
				}
			}
			List<MajiangPai> shoupaiList = panPlayerResultDbo.getPlayer().getFangruShoupaiList();
			caishenList = new ArrayList<>(panPlayerResultDbo.getPlayer().getFangruGuipaiList());
			List<ResultShoupaiVO> list = new ArrayList<>();
			resultShoupaiZuList.add(list);
			for (MajiangPai pai : shoupaiList) {
				list.add(new ResultShoupaiVO(pai));
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
