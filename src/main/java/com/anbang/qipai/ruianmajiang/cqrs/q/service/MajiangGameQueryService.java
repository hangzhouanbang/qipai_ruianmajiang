package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameState;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.GamePlayer;
import com.dml.mpgame.game.GamePlayerOnlineState;
import com.dml.mpgame.game.GameValueObject;

@Component
public class MajiangGameQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

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

}
