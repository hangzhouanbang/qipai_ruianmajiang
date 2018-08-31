package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;

public interface GameLatestPanActionFrameDboDao {

	GameLatestPanActionFrameDbo findById(String id);

	void save(String id, byte[] data);

}
