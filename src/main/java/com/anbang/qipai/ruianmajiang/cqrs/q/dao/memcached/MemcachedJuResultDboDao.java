package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedJuResultDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	public void save(JuResultDbo juResultDbo) throws Throwable {
		boolean operator = memcachedClient.set("juresult_" + juResultDbo.getGameId(), 0,
				juResultDbo.toByteArray(1024 * 16), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public JuResultDbo findByGameId(String gameId) throws Exception {
		byte[] data = memcachedClient.get("juresult_" + gameId);
		if (data == null) {
			return null;
		}
		JuResultDbo juResultDbo = JuResultDbo.fromByteArray(data);
		return juResultDbo;
	}

	public void removeJuResultDboByGameId(String gameId) {
		try {
			memcachedClient.delete("juresult_" + gameId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
