package com.anbang.qipai.ruianmajiang.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

@Configuration
public class MemcacheConfiguration {

	@Value("${memcache.ip}")
	private String memcacheIP;

	@Value("${memcache.port}")
	private int memcachedPort;

	@Bean
	public MemcachedClient memCachedClient() throws IOException {
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(memcacheIP + ":" + memcachedPort));
		return builder.build();
	}
}
