package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository.MajiangGameDboRepository;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;

@Component
public class MongodbMajiangGameDao implements MajiangGameDao {

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

}
