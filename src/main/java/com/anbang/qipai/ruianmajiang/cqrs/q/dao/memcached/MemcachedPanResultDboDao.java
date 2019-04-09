package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedPanResultDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	public void save(PanResultDbo panResultDbo) throws Throwable {
		boolean operator = memcachedClient.set("panresult_" + panResultDbo.getPanNo() + panResultDbo.getGameId(), 0,
				panResultDbo.toByteArray(1024 * 8), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public PanResultDbo findByGameIdAndPanNo(String gameId, int panNo) throws Exception {
		byte[] data = memcachedClient.get("panresult_" + panNo + gameId);
		if (data == null) {
			return null;
		}
		PanResultDbo panResultDbo = PanResultDbo.fromByteArray(data);
		return panResultDbo;
	}
}
