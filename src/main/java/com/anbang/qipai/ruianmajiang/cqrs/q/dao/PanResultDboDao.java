package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;

public interface PanResultDboDao {

	void save(PanResultDbo panResultDbo);

	PanResultDbo findByGameIdAndPanNo(String gameId, int panNo);

}
