package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.FinishResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.finish.vote.MostPlayersWinVoteCalculator;
import com.dml.mpgame.game.finish.vote.VoteAfterStartedGameFinishStrategy;
import com.dml.mpgame.game.finish.vote.VoteOption;
import com.dml.mpgame.game.join.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.game.leave.HostGameLeaveStrategy;
import com.dml.mpgame.game.ready.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.server.GameServer;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.playerCreateGame(gameId, new FixedNumberOfPlayersGameJoinStrategy(renshu),
				new FixedNumberOfPlayersGameReadyStrategy(renshu), new HostGameLeaveStrategy(playerId),
				new VoteAfterStartedGameFinishStrategy(playerId, new MostPlayersWinVoteCalculator()), playerId);
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		majiangGameManager.newMajiangGame(gameId, difen, taishu, panshu, renshu, dapao);
	}

	@Override
	public GameValueObject leaveGame(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.leave(playerId);
	}

	@Override
	public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
		ReadyForGameResult result = new ReadyForGameResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		GameValueObject gameValueObject = gameServer.ready(playerId);
		result.setGame(gameValueObject);

		List<String> playerIds = gameServer.findAllPlayerIdsForGame(gameValueObject.getId());
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);

		if (gameValueObject.getState().equals(GameState.playing)) {
			MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
			PanActionFrame firstActionFrame = majiangGameManager.createJuAndStartFirstPan(gameValueObject, currentTime);
			result.setFirstActionFrame(firstActionFrame);
		}
		return result;
	}

	@Override
	public GameValueObject joinGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.join(playerId, gameId);
	}

	@Override
	public GameValueObject backToGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.back(playerId, gameId);
	}

	@Override
	public void bindPlayer(String playerId, String gameId) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.bindPlayer(playerId, gameId);
	}

	@Override
	public FinishResult finish(String playerId) throws Exception {
		FinishResult result = new FinishResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		GameValueObject gameValueObject = gameServer.finishGame(playerId);
		result.setGameValueObject(gameValueObject);
		if (gameValueObject.getState().equals(GameState.finished)) {
			MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
			RuianMajiangJuResult juResult = majiangGameManager.finishMajiangGame(gameValueObject.getId());
			result.setJuResult(juResult);
		}
		return result;
	}

	@Override
	public FinishResult voteToFinish(String playerId, Boolean yes) throws Exception {
		FinishResult finishResult = new FinishResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		GameValueObject gameValueObject;
		Game game = gameServer.findGamePlayerPlaying(playerId);
		VoteAfterStartedGameFinishStrategy voteAfterStartedGameFinishStrategy = (VoteAfterStartedGameFinishStrategy) game
				.getFinishStrategy();
		if (yes) {
			gameValueObject = voteAfterStartedGameFinishStrategy.vote(playerId, VoteOption.yes, game);
		} else {
			gameValueObject = voteAfterStartedGameFinishStrategy.vote(playerId, VoteOption.no, game);
		}
		finishResult.setGameValueObject(gameValueObject);
		if (gameValueObject.getState().equals(GameState.finished)) {
			MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
			RuianMajiangJuResult juResult = majiangGameManager.finishMajiangGame(game.getId());
			finishResult.setJuResult(juResult);
		}
		return finishResult;
	}

}
