package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import java.util.Map;

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
import com.dml.mpgame.game.extend.fpmpv.back.OnlineGameBackStrategy;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.MostPlayersWinVoteCalculator;
import com.dml.mpgame.game.extend.vote.OnlineVotePlayersFilter;
import com.dml.mpgame.game.extend.vote.VoteOption;
import com.dml.mpgame.game.join.FixedNumberOfPlayersGameJoinStrategy;
import com.dml.mpgame.game.leave.HostGameLeaveStrategy;
import com.dml.mpgame.game.leave.NoPlayerCancelGameGameLeaveStrategy;
import com.dml.mpgame.game.leave.OfflineAndNotReadyGameLeaveStrategy;
import com.dml.mpgame.game.leave.OfflineGameLeaveStrategy;
import com.dml.mpgame.game.leave.PlayerGameLeaveStrategy;
import com.dml.mpgame.game.leave.PlayerLeaveCancelGameGameLeaveStrategy;
import com.dml.mpgame.game.player.PlayerFinished;
import com.dml.mpgame.game.ready.FixedNumberOfPlayersGameReadyStrategy;
import com.dml.mpgame.game.watch.WatcherMap;
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

		newGame.setVotePlayersFilter(new OnlineVotePlayersFilter());

		newGame.setJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));

		newGame.setLeaveByOfflineStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByOfflineStrategyBeforeStart(new OfflineAndNotReadyGameLeaveStrategy());

		newGame.setLeaveByHangupStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByHangupStrategyBeforeStart(new OfflineAndNotReadyGameLeaveStrategy());

		newGame.setLeaveByPlayerStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByPlayerStrategyBeforeStart(new HostGameLeaveStrategy(playerId));

		newGame.setBackStrategy(new OnlineGameBackStrategy());
		newGame.create(gameId, playerId);
		gameServer.playerCreateGame(newGame, playerId);

		return new MajiangGameValueObject(newGame);
	}

	@Override
	public MajiangGameValueObject newMajiangGameLeaveAndQuit(String gameId, String playerId, Integer difen,
			Integer taishu, Integer panshu, Integer renshu, Boolean dapao) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);

		MajiangGame newGame = new MajiangGame();
		newGame.setDapao(dapao);
		newGame.setDifen(difen);
		newGame.setFixedPlayerCount(renshu);
		newGame.setPanshu(panshu);
		newGame.setRenshu(renshu);
		newGame.setTaishu(taishu);

		newGame.setVotePlayersFilter(new OnlineVotePlayersFilter());

		newGame.setJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));

		newGame.setLeaveByOfflineStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByOfflineStrategyBeforeStart(new PlayerGameLeaveStrategy());

		newGame.setLeaveByHangupStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByHangupStrategyBeforeStart(new PlayerGameLeaveStrategy());

		newGame.setLeaveByPlayerStrategyAfterStart(new PlayerLeaveCancelGameGameLeaveStrategy());
		newGame.setLeaveByPlayerStrategyBeforeStart(new PlayerGameLeaveStrategy());

		newGame.setBackStrategy(new OnlineGameBackStrategy());
		newGame.create(gameId, playerId);
		gameServer.playerCreateGame(newGame, playerId);

		return new MajiangGameValueObject(newGame);
	}

	@Override
	public MajiangGameValueObject leaveGame(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		Game game = gameServer.findGamePlayerPlaying(playerId);
		MajiangGameValueObject majiangGameValueObject = gameServer.leaveByPlayer(playerId);
		if (game.getState().name().equals(FinishedByVote.name) || game.getState().name().equals(Finished.name)) {// 有可能离开的时候正在投票，由于离开自动投弃权最终导致游戏结束
			gameServer.finishGame(game.getId());
		}
		return majiangGameValueObject;
	}

	@Override
	public MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		Game game = gameServer.findGamePlayerPlaying(playerId);
		MajiangGameValueObject majiangGameValueObject = gameServer.leaveByOffline(playerId);
		if (game.getState().name().equals(FinishedByVote.name)) {// 有可能离开的时候正在投票，由于离开自动投弃权最终导致游戏结束
			gameServer.finishGame(game.getId());
		}
		return majiangGameValueObject;
	}

	@Override
	public MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		Game game = gameServer.findGamePlayerPlaying(playerId);
		MajiangGameValueObject majiangGameValueObject = gameServer.leaveByHangup(playerId);
		if (game.getState().name().equals(FinishedByVote.name)) {// 有可能离开的时候正在投票，由于离开自动投弃权最终导致游戏结束
			gameServer.finishGame(game.getId());
		}
		return majiangGameValueObject;
	}

	@Override
	public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
		ReadyForGameResult result = new ReadyForGameResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGameValueObject majiangGameValueObject = gameServer.ready(playerId, currentTime);
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
	public void bindPlayer(String playerId, String gameId) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		gameServer.bindPlayer(playerId, gameId);
	}

	@Override
	public MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGame majiangGame = (MajiangGame) gameServer.findGamePlayerPlaying(playerId);
		// 在准备阶段不会发起投票
		if (majiangGame.getState().name().equals(WaitingStart.name)) {
			// 是主机的话直接解散，不是的话自己走人
			if (majiangGame.getCreatePlayerId().equals(playerId)) {
				majiangGame.cancel();
				gameServer.finishGame(majiangGame.getId());
			} else {
				majiangGame.quit(playerId);
			}
		} else {
			majiangGame.launchVoteToFinish(playerId, new MostPlayersWinVoteCalculator(), currentTime, 99000);
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

	@Override
	public MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGame majiangGame = (MajiangGame) gameServer.findGamePlayerPlaying(playerId);
		majiangGame.voteToFinishByTimeOver(currentTime);

		if (majiangGame.getState().name().equals(FinishedByVote.name)) {
			gameServer.finishGame(majiangGame.getId());
		}
		return new MajiangGameValueObject(majiangGame);
	}

	@Override
	public ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception {
		ReadyForGameResult result = new ReadyForGameResult();
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGameValueObject majiangGameValueObject = gameServer.cancelReady(playerId, currentTime);
		result.setMajiangGame(majiangGameValueObject);

		return result;
	}

	@Override
	public MajiangGameValueObject joinWatch(String playerId, String nickName, String headimgurl, String gameId)
			throws Exception {
		WatcherMap watcherMap = singletonEntityRepository.getEntity(WatcherMap.class);
		watcherMap.join(playerId, nickName, headimgurl, gameId);
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGameValueObject majiangGameValueObject = gameServer.getInfo(playerId, gameId);
		return majiangGameValueObject;
	}

	@Override
	public MajiangGameValueObject leaveWatch(String playerId, String gameId) throws Exception {
		WatcherMap watcherMap = singletonEntityRepository.getEntity(WatcherMap.class);
		watcherMap.leave(playerId, gameId);
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		MajiangGameValueObject majiangGameValueObject = gameServer.getInfo(playerId, gameId);
		return majiangGameValueObject;
	}

	@Override
	public Map getwatch(String gameId) {
		WatcherMap watcherMap = singletonEntityRepository.getEntity(WatcherMap.class);
		return watcherMap.getWatch(gameId);
	}

	@Override
	public void recycleWatch(String gameId) {
		WatcherMap watcherMap = singletonEntityRepository.getEntity(WatcherMap.class);
		watcherMap.recycleWatch(gameId);
	}

	@Override
	public MajiangGameValueObject newMajiangGamePlayerLeaveAndQuit(String gameId, String playerId, Integer difen,
			Integer taishu, Integer panshu, Integer renshu, Boolean dapao) {
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);

		MajiangGame newGame = new MajiangGame();
		newGame.setDapao(dapao);
		newGame.setDifen(difen);
		newGame.setFixedPlayerCount(renshu);
		newGame.setPanshu(panshu);
		newGame.setRenshu(renshu);
		newGame.setTaishu(taishu);

		newGame.setVotePlayersFilter(new OnlineVotePlayersFilter());

		newGame.setJoinStrategy(new FixedNumberOfPlayersGameJoinStrategy(renshu));
		newGame.setReadyStrategy(new FixedNumberOfPlayersGameReadyStrategy(renshu));

		newGame.setLeaveByOfflineStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByOfflineStrategyBeforeStart(new OfflineAndNotReadyGameLeaveStrategy());

		newGame.setLeaveByHangupStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByHangupStrategyBeforeStart(new OfflineAndNotReadyGameLeaveStrategy());

		newGame.setLeaveByPlayerStrategyAfterStart(new OfflineGameLeaveStrategy());
		newGame.setLeaveByPlayerStrategyBeforeStart(new NoPlayerCancelGameGameLeaveStrategy());

		newGame.setBackStrategy(new OnlineGameBackStrategy());
		newGame.create(gameId, playerId);
		gameServer.playerCreateGame(newGame, playerId);

		return new MajiangGameValueObject(newGame);
	}

}
