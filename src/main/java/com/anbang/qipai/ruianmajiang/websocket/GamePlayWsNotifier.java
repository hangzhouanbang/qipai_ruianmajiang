package com.anbang.qipai.ruianmajiang.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;

@Component
public class GamePlayWsNotifier {

	private Map<String, WebSocketSession> idSessionMap = new ConcurrentHashMap<>();

	private Map<String, Long> sessionIdActivetimeMap = new ConcurrentHashMap<>();

	private Map<String, String> sessionIdPlayerIdMap = new ConcurrentHashMap<>();

	private Map<String, String> playerIdSessionIdMap = new ConcurrentHashMap<>();

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private Gson gson = new Gson();

	public WebSocketSession removeSession(String id) {
		WebSocketSession removedSession = idSessionMap.remove(id);
		sessionIdActivetimeMap.remove(id);
		if (removedSession != null) {
			String removedPlayerId = sessionIdPlayerIdMap.remove(id);
			if (removedPlayerId != null) {
				playerIdSessionIdMap.remove(removedPlayerId);
			}
		}
		return removedSession;
	}

	public void addSession(WebSocketSession session) {
		idSessionMap.put(session.getId(), session);
		sessionIdActivetimeMap.put(session.getId(), System.currentTimeMillis());
	}

	public void bindPlayer(String sessionId, String playerId) {
		String sessionAlreadyExistsId = playerIdSessionIdMap.get(playerId);
		if (sessionAlreadyExistsId != null) {
			WebSocketSession removedSession = removeSession(sessionAlreadyExistsId);
			if (removedSession != null) {
				try {
					removedSession.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		sessionIdPlayerIdMap.put(sessionId, playerId);
		playerIdSessionIdMap.put(playerId, sessionId);
		updateSession(sessionId);
	}

	public void updateSession(String id) {
		sessionIdActivetimeMap.put(id, System.currentTimeMillis());
	}

	public String findPlayerIdBySessionId(String sessionId) {
		return sessionIdPlayerIdMap.get(sessionId);
	}

	public void notifyToQuery(String playerId, String scope) {
		executorService.submit(() -> {
			System.out.println("发送worker开始执行" + "=" + playerId + "=" + "=" + scope + "=");
			CommonMO mo = new CommonMO();
			System.out.println("通知发送开始1");
			mo.setMsg("query");
			System.out.println("通知发送开始2");
			Map data = new HashMap();
			System.out.println("通知发送开始3");
			data.put("scope", scope);
			System.out.println("通知发送开始4");
			mo.setData(data);
			System.out.println("通知发送开始5");
			String payLoad = gson.toJson(mo);
			System.out.println("通知发送开始6");
			String sessionId = playerIdSessionIdMap.get(playerId);
			if (sessionId == null) {
				return;
			}
			WebSocketSession session = idSessionMap.get(sessionId);
			System.out.println("通知发送开始7");
			if (session != null) {
				try {
					// TODO 测试代码
					System.out.println("通知发送开始8");
					System.out.println("通知发送开始{" + session.isOpen() + "}：<" + playerId + "> " + payLoad + " ("
							+ System.currentTimeMillis() + ")");
					System.out.println("通知发送开始2");
					session.sendMessage(new TextMessage(payLoad));
					System.out.println("通知发送结束{" + session.isOpen() + "}：<" + playerId + "> " + payLoad + " ("
							+ System.currentTimeMillis() + ")");
				} catch (IOException e) {
					// TODO 测试代码
					System.out.println(
							"通知发送失败（ioe）：<" + playerId + "> " + payLoad + " (" + System.currentTimeMillis() + ")");
					e.printStackTrace();
				}
			} else {
				// TODO 测试代码
				System.out.println("通知发送失败（session is null）：<" + playerId + "> " + payLoad + " ("
						+ System.currentTimeMillis() + ")");
			}
		});
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void closeAndRemoveOTSessions() {
		sessionIdActivetimeMap.forEach((id, time) -> {
			if ((System.currentTimeMillis() - time) > (30 * 1000)) {
				WebSocketSession removedSession = removeSession(id);
				if (removedSession != null) {
					try {
						removedSession.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

}
