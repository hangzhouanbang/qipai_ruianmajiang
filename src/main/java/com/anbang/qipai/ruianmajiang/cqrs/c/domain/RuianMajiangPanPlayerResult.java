package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.pan.result.PanPlayerResult;

public class RuianMajiangPanPlayerResult extends PanPlayerResult {

	private RuianMajiangPanPlayerScore score;

	private int totalScore;

	public RuianMajiangPanPlayerResult() {
	}

	public RuianMajiangPanPlayerResult(String playerId) {
		super(playerId);
	}

	public RuianMajiangPanPlayerScore getScore() {
		return score;
	}

	public void setScore(RuianMajiangPanPlayerScore score) {
		this.score = score;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

}
