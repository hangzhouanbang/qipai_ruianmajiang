package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.Pan;
import com.dml.majiang.PositionDongZhuangDeterminer;
import com.dml.majiang.RandomGuipaiDeterminer;
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
			ju.setZhuangDeterminerForFirstPan(new PositionDongZhuangDeterminer());
			ju.setAvaliablePaiFiller(new NoHuapaiRandomAvaliablePaiFiller(currentTime + 1));
			ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 2));
			Pan firstPan = new Pan();
			game.allPlayerIds().forEach((pid) -> firstPan.addPlayer(pid));
			ju.setCurrentPan(firstPan);

			// 开始定位置
			ju.determinePlayersPosition();// TODO 必须强调是定第一盘的位置

			// 开始定第一盘庄家
			ju.determineZhuangForFirstPan();

			// 开始填充可用的牌
			ju.fillAvaliablePai();

			// 开始定财神
			ju.determineGuipai();

			// TODO 开始发牌

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
