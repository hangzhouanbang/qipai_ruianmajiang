package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pan.frame.PanActionFrame;

public class ReadyToNextPanResult {

	private String gameId;

	private PanActionFrame firstActionFrame;

	private List<String> otherPlayerIds = new ArrayList<>();

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public PanActionFrame getFirstActionFrame() {
		return firstActionFrame;
	}

	public void setFirstActionFrame(PanActionFrame firstActionFrame) {
		this.firstActionFrame = firstActionFrame;
	}

	public List<String> getOtherPlayerIds() {
		return otherPlayerIds;
	}

	public void setOtherPlayerIds(List<String> otherPlayerIds) {
		this.otherPlayerIds = otherPlayerIds;
	}

}
