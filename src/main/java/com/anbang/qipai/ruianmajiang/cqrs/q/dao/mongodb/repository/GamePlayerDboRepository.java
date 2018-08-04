package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerState;

public interface GamePlayerDboRepository extends MongoRepository<MajiangGamePlayerDbo, String> {

	List<MajiangGamePlayerDbo> findByGameId(String gameId);

	MajiangGamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId);

	MajiangGamePlayerDbo findByPlayerIdAndStateIsNot(String playerId, MajiangGamePlayerState state);

	void deleteByPlayerIdAndGameId(String playerId, String gameId);

}
