package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository.MajiangGameDboRepository;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.GameState;

@Component
public class MongodbMajiangGameDboDao implements MajiangGameDboDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MajiangGameDboRepository repository;

	@Override
	public MajiangGameDbo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void save(MajiangGameDbo majiangGameDbo) {
		repository.save(majiangGameDbo);
	}

	@Override
	public void update(String id, byte[] latestPanActionFrameData) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
				new Update().set("latestPanActionFrameData", latestPanActionFrameData), MajiangGameDbo.class);
	}

	@Override
	public void update(String id, GameState state) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("state", state),
				MajiangGameDbo.class);
	}

	@Override
	public void update(String id, Map<String, Boolean> nextPanPlayerReadyObj) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
				new Update().set("nextPanPlayerReadyObj", nextPanPlayerReadyObj), MajiangGameDbo.class);
	}

	@Override
	public void clearNextPanPlayerReadyObj(String id) {
		mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
				new Update().set("nextPanPlayerReadyObj", null), MajiangGameDbo.class);
	}

}
