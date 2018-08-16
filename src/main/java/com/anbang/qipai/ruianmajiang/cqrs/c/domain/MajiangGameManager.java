package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.GameValueObject;

public class MajiangGameManager {

	private Map<String, MajiangGame> gameIdMajiangGameMap = new HashMap<>();

	public void newMajiangGame(String gameId, int difen, int taishu, int panshu, int renshu, boolean dapao) {
		MajiangGame majiangGame = new MajiangGame();
		majiangGame.setDapao(dapao);
		majiangGame.setDifen(difen);
		majiangGame.setPanshu(panshu);
		majiangGame.setRenshu(renshu);
		majiangGame.setTaishu(taishu);
		majiangGame.setGameId(gameId);
		gameIdMajiangGameMap.put(gameId, majiangGame);
	}

	public PanActionFrame createJuAndStartFirstPan(GameValueObject game, long currentTime) throws Exception {
		MajiangGame majiangGame = gameIdMajiangGameMap.get(game.getId());
		return majiangGame.createJuAndStartFirstPan(game, currentTime);
	}

	public MajiangGame findGameById(String gameId) {
		return gameIdMajiangGameMap.get(gameId);
	}

	public MajiangActionResult majiangAction(String playerId, String gameId, int actionId, long actionTime)
			throws Exception {
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		MajiangActionResult majiangActionResult = game.action(playerId, actionId, actionTime);
		if (majiangActionResult.getJuResult() != null) {// 都结束了
			gameIdMajiangGameMap.remove(gameId);
		}
		return majiangActionResult;
	}

	public ReadyToNextPanResult readyToNextPan(String playerId, String gameId) throws Exception {
		ReadyToNextPanResult readyToNextPanResult = new ReadyToNextPanResult();
		readyToNextPanResult.setGameId(gameId);
		MajiangGame game = gameIdMajiangGameMap.get(gameId);
		PanActionFrame firstActionFrame = game.readyToNextPan(playerId);
		readyToNextPanResult.setFirstActionFrame(firstActionFrame);
		return readyToNextPanResult;
	}

	public RuianMajiangJuResult finishMajiangGame(String gameId) {
		MajiangGame game = gameIdMajiangGameMap.remove(gameId);
		if (game.getJu() != null) {
			return (RuianMajiangJuResult) game.finishJu();
		} else {
			return null;
		}
	}

}
