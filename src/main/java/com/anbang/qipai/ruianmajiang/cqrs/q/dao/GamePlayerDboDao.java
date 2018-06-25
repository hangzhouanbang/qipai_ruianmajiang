package com.anbang.qipai.ruianmajiang.cqrs.q.dao;

import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;

public interface GamePlayerDboDao {

	void save(GamePlayerDbo gamePlayerDbo);

	List<GamePlayerDbo> findByGameId(String gameId);

}
