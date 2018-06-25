package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;

public interface GamePlayerDboRepository extends MongoRepository<GamePlayerDbo, String> {

	List<GamePlayerDbo> findByGameId(String gameId);

}
