package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.mpgame.GamePlayerState;

public interface GamePlayerDboRepository extends MongoRepository<GamePlayerDbo, String> {

	List<GamePlayerDbo> findByGameId(String gameId);

	GamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId);

	GamePlayerDbo findByPlayerIdAndStateIsNot(String playerId, GamePlayerState state);

	void deleteByPlayerIdAndGameId(String playerId, String gameId);

}
