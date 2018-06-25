package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository.GamePlayerDboRepository;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;

@Component
public class MongodbGamePlayerDboDao implements GamePlayerDboDao {

	@Autowired
	private GamePlayerDboRepository repository;

	@Override
	public void save(GamePlayerDbo gamePlayerDbo) {
		repository.save(gamePlayerDbo);
	}

	@Override
	public List<GamePlayerDbo> findByGameId(String gameId) {
		return repository.findByGameId(gameId);
	}

	@Override
	public GamePlayerDbo findByPlayerIdAndGameId(String playerId, String gameId) {
		return repository.findByPlayerIdAndGameId(playerId, gameId);
	}

}
