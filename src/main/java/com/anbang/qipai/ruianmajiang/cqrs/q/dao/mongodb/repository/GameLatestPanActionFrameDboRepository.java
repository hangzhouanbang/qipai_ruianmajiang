package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;

public interface GameLatestPanActionFrameDboRepository extends MongoRepository<GameLatestPanActionFrameDbo, String> {

}
