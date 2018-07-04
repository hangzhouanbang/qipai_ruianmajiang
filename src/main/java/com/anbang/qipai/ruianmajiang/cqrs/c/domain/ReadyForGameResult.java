package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.mpgame.GameState;

public class ReadyForGameResult {
	private String gameId;
	private GameState gameState;
	private byte[] firstActionframeDataOfFirstPan;
	private List<String> otherPlayerIds = new ArrayList<>();;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public byte[] getFirstActionframeDataOfFirstPan() {
		return firstActionframeDataOfFirstPan;
	}

	public void setFirstActionframeDataOfFirstPan(byte[] firstActionframeDataOfFirstPan) {
		this.firstActionframeDataOfFirstPan = firstActionframeDataOfFirstPan;
	}

	public List<String> getOtherPlayerIds() {
		return otherPlayerIds;
	}

	public void setOtherPlayerIds(List<String> otherPlayerIds) {
		this.otherPlayerIds = otherPlayerIds;
	}

}
