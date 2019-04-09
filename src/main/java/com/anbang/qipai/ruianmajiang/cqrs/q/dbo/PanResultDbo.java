package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

@Document
@CompoundIndexes({ @CompoundIndex(name = "gameId_panNo_index", def = "{'gameId': 1, 'panNo': 1}") })
public class PanResultDbo implements ByteBufferAble {
	private String id;
	private String gameId;
	private int panNo;
	private String zhuangPlayerId;
	private boolean hu;
	private boolean zimo;
	private String dianpaoPlayerId;
	private List<RuianMajiangPanPlayerResultDbo> playerResultList;
	private long finishTime;
	private PanActionFrame panActionFrame;

	public byte[] toByteArray(int bufferSize) throws Throwable {
		byte[] buffer = new byte[bufferSize];
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		ByteBufferSerializer.objToByteBuffer(this, bb);
		byte[] byteArray = new byte[bb.position()];
		System.arraycopy(buffer, 0, byteArray, 0, byteArray.length);
		return byteArray;
	}

	public static PanResultDbo fromByteArray(byte[] byteArray) {
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
		ByteBufferSerializer.stringToByteBuffer(zhuangPlayerId, bb);
		ByteBufferSerializer.booleanToByteBuffer(hu, bb);
		ByteBufferSerializer.booleanToByteBuffer(zimo, bb);
		ByteBufferSerializer.stringToByteBuffer(dianpaoPlayerId, bb);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(playerResultList), bb);
		bb.putLong(finishTime);
		ByteBufferSerializer.objToByteBuffer(panActionFrame, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		gameId = ByteBufferSerializer.byteBufferToString(bb);
		panNo = bb.getInt();
		zhuangPlayerId = ByteBufferSerializer.byteBufferToString(bb);
		hu = ByteBufferSerializer.byteBufferToBoolean(bb);
		zimo = ByteBufferSerializer.byteBufferToBoolean(bb);
		dianpaoPlayerId = ByteBufferSerializer.byteBufferToString(bb);
		playerResultList = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((playerResult) -> {
			playerResultList.add((RuianMajiangPanPlayerResultDbo) playerResult);
		});
		finishTime = bb.getLong();
		panActionFrame = ByteBufferSerializer.byteBufferToObj(bb);
	}

	public PanResultDbo() {
	}

	public PanResultDbo(String gameId, RuianMajiangPanResult ruianMajiangPanResult) {
		this.gameId = gameId;
		panNo = ruianMajiangPanResult.getPan().getNo();
		zhuangPlayerId = ruianMajiangPanResult.findZhuangPlayerId();
		hu = ruianMajiangPanResult.isHu();
		zimo = ruianMajiangPanResult.isZimo();
		dianpaoPlayerId = ruianMajiangPanResult.getDianpaoPlayerId();
		playerResultList = new ArrayList<>();
		for (RuianMajiangPanPlayerResult playerResult : ruianMajiangPanResult.getPanPlayerResultList()) {
			RuianMajiangPanPlayerResultDbo dbo = new RuianMajiangPanPlayerResultDbo();
			dbo.setPlayerId(playerResult.getPlayerId());
			dbo.setPlayerResult(playerResult);
			dbo.setPlayer(ruianMajiangPanResult.findPlayer(playerResult.getPlayerId()));
			playerResultList.add(dbo);
		}

		finishTime = ruianMajiangPanResult.getPanFinishTime();
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

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZimo() {
		return zimo;
	}

	public void setZimo(boolean zimo) {
		this.zimo = zimo;
	}

	public String getDianpaoPlayerId() {
		return dianpaoPlayerId;
	}

	public void setDianpaoPlayerId(String dianpaoPlayerId) {
		this.dianpaoPlayerId = dianpaoPlayerId;
	}

	public List<RuianMajiangPanPlayerResultDbo> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResultDbo> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
