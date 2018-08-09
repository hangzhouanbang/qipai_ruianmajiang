package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.VoteToFinishResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameState;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.GamePlayer;
import com.dml.mpgame.game.GamePlayerOnlineState;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

@Component
public class MajiangGameQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	public MajiangGameDbo findMajiangGameDboById(String gameId) {
		return majiangGameDboDao.findById(gameId);
	}

	public void newMajiangGame(String newGameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		MajiangGameDbo majiangGameDbo = new MajiangGameDbo();
		majiangGameDbo.setDapao(dapao);
		majiangGameDbo.setDifen(difen);
		majiangGameDbo.setId(newGameId);
		majiangGameDbo.setPanshu(panshu);
		majiangGameDbo.setTaishu(taishu);
		majiangGameDbo.setRenshu(renshu);
		majiangGameDbo.setState(MajiangGameState.waitingStart);
		majiangGameDboDao.save(majiangGameDbo);

		joinGame(playerId, newGameId);

	}

	public List<MajiangGamePlayerDbo> findGamePlayerDbosForGame(String gameId) {
		return gamePlayerDboDao.findByGameId(gameId);
	}

	public void backToGame(String playerId, String gameId) {
		MajiangGamePlayerDbo gamePlayerDbo = gamePlayerDboDao.findByPlayerIdAndGameId(playerId, gameId);
		gamePlayerDbo.setOnlineState(GamePlayerOnlineState.online);
		gamePlayerDboDao.save(gamePlayerDbo);
	}

	public void joinGame(String playerId, String gameId) {
		PlayerInfo playerInfo = playerInfoDao.findById(playerId);
		String nickname = null;
		String headimgurl = null;
		if (playerInfo != null) {
			nickname = playerInfo.getNickname();
			headimgurl = playerInfo.getHeadimgurl();
		}

		MajiangGamePlayerDbo gamePlayerDbo = new MajiangGamePlayerDbo();
		gamePlayerDbo.setGameId(gameId);
		gamePlayerDbo.setHeadimgurl(headimgurl);
		gamePlayerDbo.setNickname(nickname);
		gamePlayerDbo.setPlayerId(playerId);
		gamePlayerDbo.setState(MajiangGamePlayerState.joined);
		gamePlayerDbo.setOnlineState(GamePlayerOnlineState.online);
		gamePlayerDboDao.save(gamePlayerDbo);
	}

	public void leaveGame(GameValueObject gameValueObject) {
		String gameId = gameValueObject.getId();
		List<MajiangGamePlayerDbo> gamePlayerDboList = gamePlayerDboDao.findByGameId(gameId);
		Map<String, GamePlayerOnlineState> onlineStateMap = new HashMap<>();
		for (GamePlayer gamePlayer : gameValueObject.getPlayers()) {
			onlineStateMap.put(gamePlayer.getId(), gamePlayer.getOnlineState());
		}
		for (MajiangGamePlayerDbo gamePlayerDbo : gamePlayerDboList) {
			String playerId = gamePlayerDbo.getPlayerId();
			if (onlineStateMap.containsKey(playerId)) {
				if (!onlineStateMap.get(playerId).equals(gamePlayerDbo.getOnlineState())) {
					gamePlayerDboDao.update(playerId, gameId, onlineStateMap.get(playerId));
				}
			} else {
				gamePlayerDboDao.removeByPlayerIdAndGameId(playerId, gameId);
			}
		}
	}

	public void launchFinishVote(VoteToFinishResult voteToFinishResult) {
		GameFinishVoteValueObject gameFinishVoteValueObject = voteToFinishResult.getVoteValueObject();
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setVote(gameFinishVoteValueObject);
		gameFinishVoteDbo.setGameId(gameFinishVoteValueObject.getGameId());
		gameFinishVoteDboDao.save(gameFinishVoteDbo);

		// 投票通过了，比赛结束。要记录结果
		RuianMajiangJuResult ruianMajiangJuResult = voteToFinishResult.getJuResult();
		if (ruianMajiangJuResult != null) {
			JuResultDbo juResultDbo = new JuResultDbo(gameFinishVoteValueObject.getGameId(), null,
					ruianMajiangJuResult);
			juResultDboDao.save(juResultDbo);
			majiangGameDboDao.update(gameFinishVoteValueObject.getGameId(), MajiangGameState.finished);
			gamePlayerDboDao.updatePlayersStateForGame(gameFinishVoteValueObject.getGameId(),
					MajiangGamePlayerState.finished);
		}
	}

}
