package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.Ju;
import com.dml.majiang.MenFengDongZhuangDeterminer;
import com.dml.majiang.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.Pan;
import com.dml.majiang.RandomGuipaiDeterminer;
import com.dml.majiang.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.ZhuangMoPaiInitialActionUpdater;
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
			ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
			ju.setZhuangDeterminerForFirstPan(new MenFengDongZhuangDeterminer());
			ju.setAvaliablePaiFiller(new NoHuapaiRandomAvaliablePaiFiller(currentTime + 1));
			ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 2));
			ju.setFaPaiStrategy(new RuianMajiangFaPaiStrategy());
			ju.setInitialActionUpdater(new ZhuangMoPaiInitialActionUpdater());

			Pan firstPan = new Pan();
			game.allPlayerIds().forEach((pid) -> firstPan.addPlayer(pid));
			ju.setCurrentPan(firstPan);

			// 开始定第一盘的门风
			ju.determinePlayersMenFengForFirstPan();

			// 开始定第一盘庄家
			ju.determineZhuangForFirstPan();

			// 开始填充可用的牌
			ju.fillAvaliablePai();

			// 开始定财神
			ju.determineGuipai();

			// 开始发牌
			ju.faPai();

			// 庄家可以摸第一张牌
			ju.updateInitialAction();

			// 庄家摸第一张牌,进入正式行牌流程
			action(ju.getCurrentPan().getZhuangPlayerId(), 1);
		}
	}

	public void action(String playerId, int actionId) throws Exception {
		ju.action(playerId, actionId);
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
