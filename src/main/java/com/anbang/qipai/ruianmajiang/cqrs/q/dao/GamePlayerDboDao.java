package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.dml.mpgame.game.GamePlayerOnlineState;

public interface GamePlayerDboDao {

	void save(MajiangGamePlayerDbo gamePlayerDbo);

	List<MajiangGamePlayerDbo> findByGameId(String gameId);

	MajiangGamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId);

	void update(String playerId, String gameId, MajiangGamePlayerState state);

	MajiangGamePlayerDbo findNotFinished(String playerId);

	void update(String playerId, String gameId, GamePlayerOnlineState gamePlayerOnlineState);

	void removeByPlayerIdAndGameId(String playerId, String gameId);

	void updatePlayersStateForGame(String gameId, MajiangGamePlayerState state);

	void updateTotalScore(String gameId, String playerId, int totalScore);

}
