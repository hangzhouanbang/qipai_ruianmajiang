package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.dml.mpgame.game.GamePlayerValueObject;
import com.dml.mpgame.game.GameState;

public class MajiangGameDbo {
	private String id;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private GameState state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private List<MajiangGamePlayerDbo> players;

	public MajiangGameDbo() {
	}

	public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
		id = majiangGame.getGameId();
		difen = majiangGame.getDifen();
		taishu = majiangGame.getTaishu();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		dapao = majiangGame.isDapao();
		state = majiangGame.getState();

		players = new ArrayList<>();
		Map<String, Integer> playeTotalScoreMap = majiangGame.getPlayeTotalScoreMap();
		for (GamePlayerValueObject playerValueObject : majiangGame.getPlayers()) {
			String playerId = playerValueObject.getId();
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			MajiangGamePlayerDbo playerDbo = new MajiangGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setGender(playerInfo.getGender());
			playerDbo.setOnlineState(playerValueObject.getOnlineState());
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerValueObject.getState());
			if (playeTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
			}
			players.add(playerDbo);
		}

	}

	public MajiangGamePlayerDbo findPlayer(String playerId) {
		for (MajiangGamePlayerDbo player : players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		return null;
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

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
	}

	public List<MajiangGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<MajiangGamePlayerDbo> players) {
		this.players = players;
	}

}
