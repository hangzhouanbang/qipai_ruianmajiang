package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.Game;
import com.dml.mpgame.HostGameLeaveStrategy;

public class MajiangGameManager {

	private Map<String, MajiangGame> gameIdMajiangGameMap = new HashMap<>();

	private Map<String, String> playerIdGameIdMap = new HashMap<>();

	public void newMajiangGame(String gameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		Game newGame = new Game();
		newGame.setLeaveStrategy(new HostGameLeaveStrategy(playerId));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));
		newGame.create(gameId, playerId);
		MajiangGame majiangGame = new MajiangGame();
		majiangGame.setDapao(dapao);
		majiangGame.setDifen(difen);
		majiangGame.setPanshu(panshu);
		majiangGame.setRenshu(renshu);
		majiangGame.setTaishu(taishu);
		majiangGame.setGame(newGame);
		gameIdMajiangGameMap.put(gameId, majiangGame);

		playerIdGameIdMap.put(playerId, gameId);
	}

	public String leave(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		game.leave(playerId);
		return gameId;
	}

	public String ready(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		game.ready(playerId, currentTime);
		return gameId;
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

}
