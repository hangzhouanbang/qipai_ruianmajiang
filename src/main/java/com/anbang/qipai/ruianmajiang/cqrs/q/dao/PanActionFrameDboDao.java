package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanActionFrameDbo;

public interface PanActionFrameDboDao {

	void save(PanActionFrameDbo dbo);

	PanActionFrameDbo findByGameIdAndPanNoAndActionNo(String gameId, int panNo, int actionNo);
}
