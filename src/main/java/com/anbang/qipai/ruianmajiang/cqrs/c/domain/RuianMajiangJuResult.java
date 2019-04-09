package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;

public class RuianMajiangJuResult implements JuResult, ByteBufferAble {

	private int finishedPanCount;

	private List<RuianMajiangJuPlayerResult> playerResultList = new ArrayList<>();

	private String dayingjiaId;

	private String datuhaoId;

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		bb.putInt(finishedPanCount);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(playerResultList), bb);
		ByteBufferSerializer.stringToByteBuffer(dayingjiaId, bb);
		ByteBufferSerializer.stringToByteBuffer(datuhaoId, bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		finishedPanCount = bb.getInt();
		playerResultList = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((playerResult) -> {
			playerResultList.add((RuianMajiangJuPlayerResult) playerResult);
		});
		dayingjiaId = ByteBufferSerializer.byteBufferToString(bb);
		datuhaoId = ByteBufferSerializer.byteBufferToString(bb);
	}

	public int getFinishedPanCount() {
		return finishedPanCount;
	}

	public void setFinishedPanCount(int finishedPanCount) {
		this.finishedPanCount = finishedPanCount;
	}

	public List<RuianMajiangJuPlayerResult> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangJuPlayerResult> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

}
