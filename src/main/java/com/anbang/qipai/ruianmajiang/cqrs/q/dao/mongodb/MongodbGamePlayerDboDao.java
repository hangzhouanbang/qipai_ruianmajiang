package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository.GamePlayerDboRepository;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.dml.mpgame.GamePlayerOnlineState;
import com.dml.mpgame.GamePlayerState;

@Component
public class MongodbGamePlayerDboDao implements GamePlayerDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

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

	@Override
	public void update(String playerId, String gameId, GamePlayerState state) {
		mongoTemplate.updateFirst(new Query(Criteria.where("playerId").is(playerId).and("gameId").is(gameId)),
				new Update().set("state", state), GamePlayerDbo.class);
	}

	@Override
	public GamePlayerDbo findNotFinished(String playerId) {
		return repository.findByPlayerIdAndStateIsNot(playerId, GamePlayerState.finished);
	}

	@Override
	public void update(String playerId, String gameId, GamePlayerOnlineState gamePlayerOnlineState) {
		mongoTemplate.updateFirst(new Query(Criteria.where("playerId").is(playerId).and("gameId").is(gameId)),
				new Update().set("onlineState", gamePlayerOnlineState), GamePlayerDbo.class);
	}

	@Override
	public void removeByPlayerIdAndGameId(String playerId, String gameId) {
		repository.deleteByPlayerIdAndGameId(playerId, gameId);
	}

}
