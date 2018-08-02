package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.mpgame.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.Game;
import com.dml.mpgame.GameValueObject;
import com.dml.mpgame.HostGameLeaveStrategy;

public class MajiangGameManager {

	private Map<String, MajiangGame> gameIdMajiangGameMap = new HashMap<>();

	/**
	 * 一台服务器对于同一个用户（一个socket）同时只能玩一场游戏。
	 */
	private Map<String, String> playerIdGameIdMap = new HashMap<>();

	public void newMajiangGame(String gameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		Game newGame = new Game();
		newGame.setGameJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
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
		JoinGameResult joinGameResult = game.join(playerId);
		playerIdGameIdMap.put(playerId, gameId);
		return joinGameResult;
	}

	/**
	 * @param playerId
	 * @return 无效操作返回null
	 * @throws Exception
	 */
	public GameValueObject leave(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			return null;
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		GameValueObject gameValueObject = game.leave(playerId);
		playerIdGameIdMap.remove(playerId);
		return gameValueObject;
	}

	public void back(String playerId, String gameId) throws Exception {
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		game.back(playerId);
		playerIdGameIdMap.put(playerId, gameId);
	}

	public ReadyForGameResult ready(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		return game.ready(playerId, currentTime);
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

	public MajiangActionResult majiangAction(String playerId, int actionId, long actionTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		return game.action(playerId, actionId, actionTime);

		// TODO 彻底结束之后要清理内存
	}

	public void bindPlayer(String playerId, String gameId) {
		playerIdGameIdMap.put(playerId, gameId);
	}

	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		return game.readyToNextPan(playerId);
	}

}
