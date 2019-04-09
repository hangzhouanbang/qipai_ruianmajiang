package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.dml.majiang.player.valueobj.MajiangPlayerValueObject;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangPanPlayerResultDbo implements ByteBufferAble {

	private String playerId;
	private RuianMajiangPanPlayerResult playerResult;
	private MajiangPlayerValueObject player;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(playerId, bb);
		ByteBufferSerializer.objToByteBuffer(playerResult, bb);
		ByteBufferSerializer.objToByteBuffer(player, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		playerId = ByteBufferSerializer.byteBufferToString(bb);
		playerResult = ByteBufferSerializer.byteBufferToObj(bb);
		player = ByteBufferSerializer.byteBufferToObj(bb);
	}

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
