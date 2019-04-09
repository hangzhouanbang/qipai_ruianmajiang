package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

@Document
@CompoundIndexes({ @CompoundIndex(name = "gameId_1_panNo_1", def = "{'gameId': 1, 'panNo': 1}") })
public class PanActionFrameDbo implements ByteBufferAble {
	private String id;
	private String gameId;
	private int panNo;
	private int actionNo;
	private PanActionFrame panActionFrame;

	public byte[] toByteArray(int bufferSize) throws Throwable {
		byte[] buffer = new byte[bufferSize];
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		ByteBufferSerializer.objToByteBuffer(this, bb);
		byte[] byteArray = new byte[bb.position()];
		System.arraycopy(buffer, 0, byteArray, 0, byteArray.length);
		return byteArray;
	}

	public static PanActionFrameDbo fromByteArray(byte[] byteArray) {
		try {
			return ByteBufferSerializer.byteBufferToObj(ByteBuffer.wrap(byteArray));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(gameId, bb);
		bb.putInt(panNo);
		bb.putInt(actionNo);
		ByteBufferSerializer.objToByteBuffer(panActionFrame, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		gameId = ByteBufferSerializer.byteBufferToString(bb);
		panNo = bb.getInt();
		actionNo = bb.getInt();
		panActionFrame = ByteBufferSerializer.byteBufferToObj(bb);
	}

	public PanActionFrameDbo() {

	}

	public PanActionFrameDbo(String gameId, int panNo, int actionNo) {
		this.gameId = gameId;
		this.panNo = panNo;
		this.actionNo = actionNo;
	}

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

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public int getActionNo() {
		return actionNo;
	}

	public void setActionNo(int actionNo) {
		this.actionNo = actionNo;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
