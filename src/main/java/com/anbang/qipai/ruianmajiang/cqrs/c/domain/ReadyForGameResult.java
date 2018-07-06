package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.List;

import com.dml.mpgame.GameValueObject;

public class ReadyForGameResult {
	private GameValueObject game;
	private byte[] firstActionframeDataOfFirstPan;
	private List<String> otherPlayerIds = new ArrayList<>();;

	public GameValueObject getGame() {
		return game;
	}

	public void setGame(GameValueObject game) {
		this.game = game;
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
