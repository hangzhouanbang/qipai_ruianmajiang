package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.dml.mpgame.game.finish.vote.GameFinishVoteValueObject;

public interface GameFinishVoteDboDao {

	void save(GameFinishVoteDbo gameFinishVoteDbo);

	void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject);

	GameFinishVoteDbo findByGameId(String gameId);

	void removeGameFinishVoteDboByGameId(String gameId);
}
