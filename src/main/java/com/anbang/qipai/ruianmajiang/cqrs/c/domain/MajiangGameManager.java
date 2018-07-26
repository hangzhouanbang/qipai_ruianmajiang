package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dml.majiang.PanActionFrame;
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
		game.join(playerId);
		playerIdGameIdMap.put(playerId, gameId);
		JoinGameResult result = new JoinGameResult();
		result.setGameId(gameId);
		List<String> playerIds = game.getGame().allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);
		return result;
		// TODO 最好要和ready一样，返回GameValueObject。
	}

	public String leave(String playerId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		game.leave(playerId);
		playerIdGameIdMap.remove(playerId);
		return gameId;
		// TODO 要和ready一样，返回GameValueObject。否则q端有bug
	}

	public ReadyForGameResult ready(String playerId, long currentTime) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		PanActionFrame panActionFrame = game.ready(playerId, currentTime);
		ReadyForGameResult result = new ReadyForGameResult();
		result.setGame(new GameValueObject(game.getGame()));
		result.setFirstActionFrame(panActionFrame);

		List<String> playerIds = game.getGame().allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);

		return result;
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

	public MajiangActionResult majiangAction(String playerId, int actionId) throws Exception {
		String gameId = playerIdGameIdMap.get(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		PanActionFrame panActionFrame = game.action(playerId, actionId);
		MajiangActionResult result = new MajiangActionResult();
		// action之后要试探一盘是否结束
		if (game.shouldFinishCurrentPan()) {
			RuianMajiangPanResult ruianMajiangPanResult = game.finishCurrentPan();
			result.setResult(ruianMajiangPanResult);
		}
		result.setGame(new GameValueObject(game.getGame()));
		result.setPanActionFrame(panActionFrame);
		List<String> playerIds = game.getGame().allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);
		return result;
	}

}
