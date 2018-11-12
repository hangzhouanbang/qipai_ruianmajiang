package com.anbang.qipai.ruianmajiang.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.anbang.qipai.ruianmajiang.plan.dao.mongodb.repository.PlayerInfoRepository;

@Component
public class MongodbPlayerInfoDao implements PlayerInfoDao {

	@Autowired
	private PlayerInfoRepository repository;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public PlayerInfo findById(String id) {
		return repository.findOne(id);
	}

	@Override
	public void save(PlayerInfo playerInfo) {
		repository.save(playerInfo);
	}

	@Override
	public void updateVip(String playerId, boolean vip) {
		Query query = new Query(Criteria.where("id").is(playerId));
		Update update = new Update();
		update.set("vip", vip);
		mongoTemplate.updateFirst(query, update, PlayerInfo.class);
	}

}
