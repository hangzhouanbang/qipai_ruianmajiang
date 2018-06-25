package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.mpgame.GamePlayerNotFoundException;
import com.dml.mpgame.proxy.GameProxy;

public class MajiangGame {
	private GameProxy game;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;

	public void leave(String playerId) throws GamePlayerNotFoundException {
		game.leave(playerId);
	}

	public GameProxy getGame() {
		return game;
	}

	public void setGame(GameProxy game) {
		this.game = game;
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

}
