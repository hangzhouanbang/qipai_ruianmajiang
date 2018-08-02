package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.util.Map;

import com.dml.mpgame.GameState;

public class MajiangGameDbo {
	private String id;// 就是gameid
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private GameState state;
	private byte[] latestPanActionFrameData;
	private Map<String, Boolean> nextPanPlayerReadyObj;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDifen() {
		return difen;
	}

	public void setDifen(int difen) {
		this.difen = difen;
	}

	public int getTaishu() {
		return taishu;
	}

	public void setTaishu(int taishu) {
		this.taishu = taishu;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public byte[] getLatestPanActionFrameData() {
		return latestPanActionFrameData;
	}

	public void setLatestPanActionFrameData(byte[] latestPanActionFrameData) {
		this.latestPanActionFrameData = latestPanActionFrameData;
	}

	public Map<String, Boolean> getNextPanPlayerReadyObj() {
		return nextPanPlayerReadyObj;
	}

	public void setNextPanPlayerReadyObj(Map<String, Boolean> nextPanPlayerReadyObj) {
		this.nextPanPlayerReadyObj = nextPanPlayerReadyObj;
	}

}
