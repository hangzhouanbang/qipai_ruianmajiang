package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteValueObjectDbo;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedGameFinishVoteDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	public void save(GameFinishVoteDbo gameFinishVoteDbo) throws Throwable {
		boolean operator = memcachedClient.set("vote_" + gameFinishVoteDbo.getGameId(), 0,
				gameFinishVoteDbo.toByteArray(1024 * 1), 7 * 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public void update(String gameId, GameFinishVoteValueObject gameFinishVoteValueObject) throws Throwable {
		GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
		gameFinishVoteDbo.setGameId(gameId);
		gameFinishVoteDbo.setVote(new GameFinishVoteValueObjectDbo(gameFinishVoteValueObject));
		boolean operator = memcachedClient.set("vote_" + gameFinishVoteDbo.getGameId(), 0,
				gameFinishVoteDbo.toByteArray(1024 * 1), 7 * 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public GameFinishVoteDbo findByGameId(String gameId) throws Exception {
		byte[] data = memcachedClient.get("vote_" + gameId);
		if (data == null) {
			return null;
		}
		GameFinishVoteDbo gameFinishVoteDbo = GameFinishVoteDbo.fromByteArray(data);
		return gameFinishVoteDbo;
	}

	public void removeGameFinishVoteDboByGameId(String gameId) {
		try {
			memcachedClient.delete("vote_" + gameId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
