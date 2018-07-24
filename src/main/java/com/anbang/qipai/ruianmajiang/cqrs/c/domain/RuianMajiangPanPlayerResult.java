package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;
import java.util.Set;

import com.dml.majiang.MajiangPai;
import com.dml.majiang.ShoupaiPaiXing;

public class RuianMajiangPanPlayerResult {

	private String playerId;

	private boolean hu;

	private RuianMajiangPanPlayerScore score;

	private ShoupaiPaiXing bestShoupaiPaiXing;

	/**
	 * 手牌列表（包含鬼牌和刚摸的，不包含公开牌）
	 */
	private List<MajiangPai> shoupaiList;

	/**
	 * 标示什么牌是鬼牌
	 */
	private Set<MajiangPai> guipaiTypeSet;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public RuianMajiangPanPlayerScore getScore() {
		return score;
	}

	public void setScore(RuianMajiangPanPlayerScore score) {
		this.score = score;
	}

	public ShoupaiPaiXing getBestShoupaiPaiXing() {
		return bestShoupaiPaiXing;
	}

	public void setBestShoupaiPaiXing(ShoupaiPaiXing bestShoupaiPaiXing) {
		this.bestShoupaiPaiXing = bestShoupaiPaiXing;
	}

	public List<MajiangPai> getShoupaiList() {
		return shoupaiList;
	}

	public void setShoupaiList(List<MajiangPai> shoupaiList) {
		this.shoupaiList = shoupaiList;
	}

	public Set<MajiangPai> getGuipaiTypeSet() {
		return guipaiTypeSet;
	}

	public void setGuipaiTypeSet(Set<MajiangPai> guipaiTypeSet) {
		this.guipaiTypeSet = guipaiTypeSet;
	}

}
