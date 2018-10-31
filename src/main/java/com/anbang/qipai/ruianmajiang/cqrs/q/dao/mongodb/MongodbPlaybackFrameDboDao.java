package com.anbang.qipai.ruianmajiang.cqrs.q.dao.mongodb;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PlaybackDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PlaybackFrameDbo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class MongodbPlaybackFrameDboDao implements PlaybackDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(PlaybackFrameDbo panActionFrame) {
        this.mongoTemplate.save(panActionFrame);
    }

    @Override
    public PlaybackFrameDbo find(String gameId, int panno, int frameNo) {
        Query query = new Query(Criteria.where("gameId").is(gameId).and("panno").is(panno).and("frameNo").is(frameNo));
        return mongoTemplate.findOne(query, PlaybackFrameDbo.class);
    }

    @Override
    public int lastFrameNo(String gameId, int panno) {
        Query query = new Query(Criteria.where("gameId").is(gameId).and("panno").is(panno));
        return (int) mongoTemplate.count(query, PlaybackFrameDbo.class);
    }

}
