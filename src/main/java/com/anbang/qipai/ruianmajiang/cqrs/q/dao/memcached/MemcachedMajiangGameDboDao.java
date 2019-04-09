package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.mpgame.game.player.GamePlayerOnlineState;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedMajiangGameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	public MajiangGameDbo findById(String id) throws Exception {
		byte[] data = memcachedClient.get("gameinfo_" + id);
		if (data == null) {
			return null;
		}
		MajiangGameDbo majiangGameDbo = MajiangGameDbo.fromByteArray(data);
		return majiangGameDbo;
	}

	public void save(MajiangGameDbo majiangGameDbo) throws Throwable {
		boolean operator = memcachedClient.set("gameinfo_" + majiangGameDbo.getId(), 0,
				majiangGameDbo.toByteArray(1024 * 8), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public void updatePlayerOnlineState(String id, String playerId, GamePlayerOnlineState onlineState)
			throws Throwable {
		byte[] data = memcachedClient.get("gameinfo_" + id);
		if (data == null) {
			return;
		}
		MajiangGameDbo majiangGameDbo = MajiangGameDbo.fromByteArray(data);
		majiangGameDbo.getPlayers().forEach((player) -> {
			if (player.getPlayerId().equals(playerId)) {
				player.setOnlineState(onlineState);
			}
		});
		boolean operator = memcachedClient.set("gameinfo_" + majiangGameDbo.getId(), 0,
				majiangGameDbo.toByteArray(1024 * 8), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}
}
