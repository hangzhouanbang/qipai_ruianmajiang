package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanActionFrameDbo;

public interface PanActionFrameDboDao {

	void save(PanActionFrameDbo dbo);

	List<PanActionFrameDbo> findByGameIdAndPanNo(String gameId, int panNo);
}
