package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.List;
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

	public JoinGameResult join(String playerId, String gameId) throws Exception {
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		if (game == null) {
			throw new MajinagGameNotFoundException();
		}
		game.join(playerId);
		JoinGameResult result = new JoinGameResult();
		result.setGameId(gameId);
		List<String> playerIds = game.getGame().allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);
		return result;
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

	public ReadyForGameResult ready(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		byte[] panActionFrameData = game.ready(playerId, currentTime);
		ReadyForGameResult result = new ReadyForGameResult();
		result.setGameId(gameId);
		result.setGameState(game.getGame().getState());
		result.setFirstActionframeDataOfFirstPan(panActionFrameData);

		List<String> playerIds = game.getGame().allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);

		return result;
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

}
