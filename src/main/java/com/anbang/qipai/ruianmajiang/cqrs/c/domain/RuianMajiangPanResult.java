package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;
import java.util.List;

import com.dml.majiang.PanResult;

public class RuianMajiangPanResult implements PanResult {

	private List<RuianMajiangPanPlayerScore> playerScoreList;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		// TODO Auto-generated method stub

	}

	public List<RuianMajiangPanPlayerScore> getPlayerScoreList() {
		return playerScoreList;
	}

	public void setPlayerScoreList(List<RuianMajiangPanPlayerScore> playerScoreList) {
		this.playerScoreList = playerScoreList;
	}

}
