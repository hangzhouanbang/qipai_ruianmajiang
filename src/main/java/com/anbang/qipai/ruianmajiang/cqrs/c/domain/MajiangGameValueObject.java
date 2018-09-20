package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.result.JuResult;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGameValueObject;

public class MajiangGameValueObject extends FixedPlayersMultipanAndVotetofinishGameValueObject {
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();
	private JuResult juResult;

	public MajiangGameValueObject(MajiangGame majiangGame) {
		super(majiangGame);
		difen = majiangGame.getDifen();
		taishu = majiangGame.getTaishu();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		dapao = majiangGame.isDapao();
		playeTotalScoreMap.putAll(majiangGame.getPlayeTotalScoreMap());
		juResult = majiangGame.getJu().getJuResult();
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

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

	public JuResult getJuResult() {
		return juResult;
	}

	public void setJuResult(JuResult juResult) {
		this.juResult = juResult;
	}

}
