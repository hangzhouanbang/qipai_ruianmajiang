package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;

public class RuianMajiangPanPlayerResultDbo {

	private String playerId;
	private RuianMajiangPanPlayerResult playerResult;
	private MajiangPlayerValueObject player;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public RuianMajiangPanPlayerResult getPlayerResult() {
		return playerResult;
	}

	public void setPlayerResult(RuianMajiangPanPlayerResult playerResult) {
		this.playerResult = playerResult;
	}

	public MajiangPlayerValueObject getPlayer() {
		return player;
	}

	public void setPlayer(MajiangPlayerValueObject player) {
		this.player = player;
	}

}
