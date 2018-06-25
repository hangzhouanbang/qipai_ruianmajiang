package com.anbang.qipai.ruianmajiang.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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

	/**
	 * 是否是刚连上还没发过心跳消息的
	 * 
	 * @param id
	 * @return
	 */
	public boolean isRawSession(String id) {
		return sessionIdPlayerIdMap.get(id) == null;
	}

	public void addSession(WebSocketSession session) {
		idSessionMap.put(session.getId(), session);
		sessionIdActivetimeMap.put(session.getId(), System.currentTimeMillis());
	}

	public void updateSession(String sessionId, String playerId) throws SessionAlreadyExistsException {
		if (playerIdSessionIdMap.containsKey(playerId)) {
			throw new SessionAlreadyExistsException();
		}
		sessionIdActivetimeMap.put(sessionId, System.currentTimeMillis());
		sessionIdPlayerIdMap.put(sessionId, playerId);
		playerIdSessionIdMap.put(playerId, sessionId);
	}

	public void updateSession(String id) {
		sessionIdActivetimeMap.put(id, System.currentTimeMillis());
	}

	public String findPlayerIdBySessionId(String sessionId) {
		return sessionIdPlayerIdMap.get(sessionId);
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
