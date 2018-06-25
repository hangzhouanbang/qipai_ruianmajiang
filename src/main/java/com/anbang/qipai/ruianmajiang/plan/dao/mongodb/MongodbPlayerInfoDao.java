package com.anbang.qipai.ruianmajiang.plan.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.anbang.qipai.ruianmajiang.plan.dao.mongodb.repository.PlayerInfoRopository;

@Component
public class MongodbPlayerInfoDao implements PlayerInfoDao {

	@Autowired
	private PlayerInfoRopository repository;

	@Override
	public PlayerInfo findById(String id) {
		return repository.findOne(id);
	}

}
