package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private int panNo;
	private GameState state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private List<MajiangGamePlayerDbo> players;
	private Set<String> xipaiPlayerIds;

	public MajiangGameDbo() {
	}

	public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
		id = majiangGame.getId();
		difen = majiangGame.getDifen();
		taishu = majiangGame.getTaishu();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		dapao = majiangGame.isDapao();
		panNo = majiangGame.getPanNo();
		state = majiangGame.getState();
		xipaiPlayerIds = new HashSet<>(majiangGame.getXipaiPlayerIds());
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

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
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

	public Set<String> getXipaiPlayerIds() {
		return xipaiPlayerIds;
	}

	public void setXipaiPlayerIds(Set<String> xipaiPlayerIds) {
		this.xipaiPlayerIds = xipaiPlayerIds;
	}

}
