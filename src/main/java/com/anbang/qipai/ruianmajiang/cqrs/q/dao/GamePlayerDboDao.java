package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.mpgame.GamePlayerState;

public interface GamePlayerDboDao {

	void save(GamePlayerDbo gamePlayerDbo);

	List<GamePlayerDbo> findByGameId(String gameId);

	GamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId);

	void update(String playerId, String gameId, GamePlayerState state);

}
