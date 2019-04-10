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
		builder.setConnectionPoolSize(20);
		// 设置发送缓冲区为16K，默认为8K
		// builder.setSocketOption(StandardSocketOption.SO_SNDBUF,16 *1024);
		// 启用nagle算法，提高吞吐量，默认关闭
		// builder.setSocketOption(StandardSocketOption.TCP_NODELAY,false);
		// 默认如果连接超过5秒没有任何IO操作发生即认为空闲并发起心跳检测，你可以调长这个时间：  
		builder.getConfiguration().setSessionIdleTimeout(10000); // 设置为10秒;
		return builder.build();
	}
}
