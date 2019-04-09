package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

@Document
public class GameFinishVoteDbo implements ByteBufferAble {

	private String id;
	@Indexed(unique = true)
	private String gameId;
	private GameFinishVoteValueObjectDbo vote;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public GameFinishVoteValueObjectDbo getVote() {
		return vote;
	}

	public void setVote(GameFinishVoteValueObjectDbo vote) {
		this.vote = vote;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(gameId, bb);
		ByteBufferSerializer.objToByteBuffer(vote, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		gameId = ByteBufferSerializer.byteBufferToString(bb);
		vote = ByteBufferSerializer.byteBufferToObj(bb);
	}

	public byte[] toByteArray(int bufferSize) throws Throwable {
		byte[] buffer = new byte[bufferSize];
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		ByteBufferSerializer.objToByteBuffer(this, bb);
		byte[] byteArray = new byte[bb.position()];
		System.arraycopy(buffer, 0, byteArray, 0, byteArray.length);
		return byteArray;
	}

	public static GameFinishVoteDbo fromByteArray(byte[] byteArray) {
		try {
			return ByteBufferSerializer.byteBufferToObj(ByteBuffer.wrap(byteArray));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
