package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository.GameFinishVoteDboRepository;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;

@Component
public class MongodbGameFinishVoteDboDao implements GameFinishVoteDboDao {

	@Autowired
	private GameFinishVoteDboRepository repository;

	@Override
	public void save(GameFinishVoteDbo gameFinishVoteDbo) {
		repository.save(gameFinishVoteDbo);
	}

}
