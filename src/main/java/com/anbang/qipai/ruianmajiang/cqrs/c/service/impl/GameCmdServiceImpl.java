package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGame;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Game;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.fpmpv.back.FpmpvBackStrategy;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.MostPlayersWinVoteCalculator;
import com.dml.mpgame.game.extend.vote.VoteOption;
import com.dml.mpgame.game.extend.vote.leave.VoteWaiverLeaveStrategy;
import com.dml.mpgame.game.join.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.game.leave.OfflineGameLeaveStrategy;
import com.dml.mpgame.game.player.PlayerFinished;
import com.dml.mpgame.game.ready.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.server.GameServer;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu,
			Integer panshu, Integer renshu, Boolean dapao) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);

		MajiangGame newGame = new MajiangGame();
		newGame.setDapao(dapao);
		newGame.setDifen(difen);
		newGame.setFixedPlayerCount(renshu);
		newGame.setPanshu(panshu);
		newGame.setRenshu(renshu);
		newGame.setTaishu(taishu);

		newGame.setJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));
		newGame.setLeaveStrategyBeforeStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveStrategyAfterStart(new VoteWaiverLeaveStrategy());
		newGame.setBackStrategy(new FpmpvBackStrategy());
		newGame.create(gameId, playerId);
		gameServer.playerCreateGame(newGame, playerId);

		return new MajiangGameValueObject(newGame);
	}

	@Override
	public MajiangGameValueObject leaveGame(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		Game game = gameServer.findGamePlayerPlaying(playerId);
		MajiangGameValueObject majiangGameValueObject = gameServer.leave(playerId);
		if (game.getState().name().equals(FinishedByVote.name)) {// 有可能离开的时候正在投票，由于离开自动投弃权最终导致游戏结束
			gameServer.finishGame(game.getId());
		}
		return majiangGameValueObject;
	}

	@Override
	public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
		ReadyForGameResult result = new ReadyForGameResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGameValueObject majiangGameValueObject = gameServer.ready(playerId);
		result.setMajiangGame(majiangGameValueObject);

		if (majiangGameValueObject.getState().name().equals(Playing.name)) {
			MajiangGame majiangGame = (MajiangGame) gameServer.findGamePlayerPlaying(playerId);
			PanActionFrame firstActionFrame = majiangGame.createJuAndStartFirstPan(currentTime);
			result.setFirstActionFrame(firstActionFrame);
		}
		return result;
	}

	@Override
	public MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.join(playerId, gameId);
	}

	@Override
	public MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		return gameServer.back(playerId, gameId);
	}

	@Override
	public void bindPlayer(String playerId, String gameId) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.bindPlayer(playerId, gameId);
	}

	@Override
	public MajiangGameValueObject finish(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGame majiangGame = (MajiangGame) gameServer.findGamePlayerPlaying(playerId);
		// 在准备阶段不会发起投票
		if (majiangGame.getState().name().equals(WaitingStart.name)) {
			// 是主机的话直接解散，不是的话自己走人
			if (majiangGame.getCreatePlayerId().equals(playerId)) {
				majiangGame.cancel();
			} else {
				majiangGame.quit(playerId);
			}
		} else {
			majiangGame.launchVoteToFinish(playerId, new MostPlayersWinVoteCalculator());
			majiangGame.voteToFinish(playerId, VoteOption.yes);
		}

		if (majiangGame.getState().name().equals(FinishedByVote.name)) {
			gameServer.finishGame(majiangGame.getId());
		}
		return new MajiangGameValueObject(majiangGame);
	}

	@Override
	public MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGame majiangGame = (MajiangGame) gameServer.findGamePlayerPlaying(playerId);
		if (yes) {
			majiangGame.voteToFinish(playerId, VoteOption.yes);
		} else {
			majiangGame.voteToFinish(playerId, VoteOption.no);
		}

		if (majiangGame.getState().name().equals(FinishedByVote.name)) {
			gameServer.finishGame(majiangGame.getId());
		}
		return new MajiangGameValueObject(majiangGame);
	}

	@Override
	public MajiangGameValueObject finishGameImmediately(String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGame majiangGame = (MajiangGame) gameServer.findGame(gameId);
		majiangGame.finish();
		majiangGame.setState(new Finished());
		majiangGame.updateAllPlayersState(new PlayerFinished());
		gameServer.finishGame(gameId);
		return new MajiangGameValueObject(majiangGame);
	}

}
