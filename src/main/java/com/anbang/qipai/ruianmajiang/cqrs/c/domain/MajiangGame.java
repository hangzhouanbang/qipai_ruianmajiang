package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.Pan;
import com.dml.majiang.RandomMustHasDongPlayersPositionDeterminer;
import com.dml.mpgame.Game;
import com.dml.mpgame.GameState;

public class MajiangGame {
	private Game game;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Ju ju;

	public void leave(String playerId) throws Exception {
		game.leave(playerId);
	}

	public void ready(String playerId, long currentTime) throws Exception {
		game.ready(playerId);
		if (game.getState().equals(GameState.playing)) {// 游戏开始了，那么要创建新的局
			ju = new Ju();
			ju.setPlayersPositionDeterminer(new RandomMustHasDongPlayersPositionDeterminer(currentTime));
			Pan firstPan = new Pan();
			game.allPlayerIds().forEach((pid) -> firstPan.addPlayer(pid));
			ju.setCurrentPan(firstPan);

			// 开始定位置
			ju.determinePlayersPosition();
			// TODO 开始定庄家
		}
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
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
