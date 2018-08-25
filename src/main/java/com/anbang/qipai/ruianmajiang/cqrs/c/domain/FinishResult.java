package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.mpgame.game.finish.vote.VoteAfterStartedGameFinishStrategyValueObject;

public class FinishResult {
	private VoteAfterStartedGameFinishStrategyValueObject voteFinishStrategy;
	private MajiangGameValueObject majiangGameValueObject;
	private RuianMajiangJuResult juResult;

	public VoteAfterStartedGameFinishStrategyValueObject getVoteFinishStrategy() {
		return voteFinishStrategy;
	}

	public void setVoteFinishStrategy(VoteAfterStartedGameFinishStrategyValueObject voteFinishStrategy) {
		this.voteFinishStrategy = voteFinishStrategy;
	}

	public MajiangGameValueObject getMajiangGameValueObject() {
		return majiangGameValueObject;
	}

	public void setMajiangGameValueObject(MajiangGameValueObject majiangGameValueObject) {
		this.majiangGameValueObject = majiangGameValueObject;
	}

	public RuianMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(RuianMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
