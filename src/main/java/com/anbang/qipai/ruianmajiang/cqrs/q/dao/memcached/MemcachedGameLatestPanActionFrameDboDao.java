package com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached;

import java.lang.reflect.Type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.rubyeye.xmemcached.MemcachedClient;

@Component
public class MemcachedGameLatestPanActionFrameDboDao {

	@Autowired
	private MemcachedClient memcachedClient;

	private Gson gson = new Gson();

	public GameLatestPanActionFrameDbo findById(String id) throws Exception {
		Type type = new TypeToken<GameLatestPanActionFrameDbo>() {
		}.getType();
		String json = memcachedClient.get("latest" + id);
		if (json == null) {
			return null;
		}
		GameLatestPanActionFrameDbo dbo = gson.fromJson(json, type);
		return dbo;
	}

	public void save(String id, byte[] data) throws Exception {
		GameLatestPanActionFrameDbo dbo = new GameLatestPanActionFrameDbo();
		dbo.setId(id);
		dbo.setData(data);
		boolean operator = memcachedClient.set("latest" + id, 0, gson.toJson(dbo), 24 * 60 * 60 * 1000);
		if (!operator) {
			throw new MemcachedException();
		}
	}
}
