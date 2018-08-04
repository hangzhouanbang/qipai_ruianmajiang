package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;

public class GameVO {
	private String id;// 就是gameid
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private List<MajiangGamePlayerVO> playerList;

	public GameVO(MajiangGameDbo majiangGameDbo, List<MajiangGamePlayerDbo> gamePlayerDboListForGame) {
		id = majiangGameDbo.getId();
		difen = majiangGameDbo.getDifen();
		taishu = majiangGameDbo.getTaishu();
		panshu = majiangGameDbo.getPanshu();
		renshu = majiangGameDbo.getRenshu();
		dapao = majiangGameDbo.isDapao();
		playerList = new ArrayList<>();
		gamePlayerDboListForGame.forEach((dbo) -> playerList.add(new MajiangGamePlayerVO(dbo)));
	}

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

	public List<MajiangGamePlayerVO> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<MajiangGamePlayerVO> playerList) {
		this.playerList = playerList;
	}

}
