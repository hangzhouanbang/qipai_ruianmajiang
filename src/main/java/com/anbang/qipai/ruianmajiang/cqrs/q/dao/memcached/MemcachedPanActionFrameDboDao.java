package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanActionFrameDbo;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedPanActionFrameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	public void save(PanActionFrameDbo dbo) throws Throwable {
		boolean operator = memcachedClient.set(dbo.getGameId() + "_" + dbo.getActionNo(), 0, dbo.toByteArray(1024 * 4),
				24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}

	public List<PanActionFrameDbo> findByGameIdAndActionNo(String gameId, int panNo, int lastestActionNo)
			throws Exception {
		final List<String> keys = new ArrayList<>();
		for (int i = 0; i <= lastestActionNo; i++) {
			keys.add(gameId + "_" + i);
		}
		Map<String, byte[]> frameMap = memcachedClient.get(keys);
		List<PanActionFrameDbo> frameList = new ArrayList<>();
		for (byte[] data : frameMap.values()) {
			frameList.add(PanActionFrameDbo.fromByteArray(data));
		}
		return frameList;
	}

	/**
	 * memcached不提供批量删除，如果循环删除则代价太高，推荐通过key来覆盖，减少缓存。故不使用该方法
	 */
	public void removePanActionFrame(String gameId, int panNo, int lastestActionNo) {
		for (int i = 0; i <= lastestActionNo; i++) {
			try {
				memcachedClient.delete(gameId + "_" + i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
