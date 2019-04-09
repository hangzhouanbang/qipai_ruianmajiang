package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;
import com.dml.mpgame.game.extend.vote.VoteOption;

public class PlayerVoteDbo implements ByteBufferAble {
	private String playerId;
	private VoteOption vote;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public VoteOption getVote() {
		return vote;
	}

	public void setVote(VoteOption vote) {
		this.vote = vote;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(playerId, bb);
		bb.put((byte) vote.ordinal());
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		playerId = ByteBufferSerializer.byteBufferToString(bb);
		vote = VoteOption.valueOf(bb.get());
	}
}
