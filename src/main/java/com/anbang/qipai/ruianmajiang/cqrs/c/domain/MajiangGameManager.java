package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.Game;

public class MajiangGameManager {

	private Map<String, Game> idGameMap = new HashMap<>();

	public void newMajiangGame(String gameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		Game newGame = new Game();
		newGame.setId(gameId);
		newGame.setCreatePlayerId(playerId);
		idGameMap.put(gameId, newGame);

		// TODO: 创建麻将 “局”
	}

}
