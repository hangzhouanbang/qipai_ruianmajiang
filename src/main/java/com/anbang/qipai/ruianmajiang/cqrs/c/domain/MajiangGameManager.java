package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.Game;
import com.dml.mpgame.proxy.GameProxy;
import com.dml.mpgame.proxy.HostGameProxy;

public class MajiangGameManager {

	private Map<String, GameProxy> gameIdGameProxyMap = new HashMap<>();

	public void newMajiangGame(String gameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		Game newGame = new Game();
		newGame.create(gameId, playerId);
		gameIdGameProxyMap.put(gameId, new HostGameProxy(newGame));

		// TODO: 创建麻将 “局”
	}

}
