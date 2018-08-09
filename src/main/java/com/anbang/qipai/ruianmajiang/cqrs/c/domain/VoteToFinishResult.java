package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

public class VoteToFinishResult {

	private GameFinishVoteValueObject voteValueObject;
	private RuianMajiangJuResult juResult;

	public GameFinishVoteValueObject getVoteValueObject() {
		return voteValueObject;
	}

	public void setVoteValueObject(GameFinishVoteValueObject voteValueObject) {
		this.voteValueObject = voteValueObject;
	}

	public RuianMajiangJuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(RuianMajiangJuResult juResult) {
		this.juResult = juResult;
	}

}
