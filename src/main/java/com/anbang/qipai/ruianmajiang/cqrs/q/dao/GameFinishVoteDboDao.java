package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.finish.GameFinishVoteValueObject;

public interface GameFinishVoteDboDao {

	void save(GameFinishVoteDbo gameFinishVoteDbo);

	void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject);

	GameFinishVoteDbo findByGameId(String gameId);

	void removeGameFinishVoteDboByGameId(String gameId);
}
