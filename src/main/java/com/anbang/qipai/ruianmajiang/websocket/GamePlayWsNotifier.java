package com.anbang.qipai.ruianmajiang.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Map<String, Set<String>> playerIdSessionIdsMap = new ConcurrentHashMap<>();

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private Gson gson = new Gson();

	private Logger logger = LoggerFactory.getLogger(getClass());

	public WebSocketSession removeSession(String id) {
		WebSocketSession removedSession = idSessionMap.remove(id);
		sessionIdActivetimeMap.remove(id);
		if (removedSession != null) {
			String removedPlayerId = sessionIdPlayerIdMap.remove(id);
			if (removedPlayerId != null) {
				Set<String> currentSessionIdSetForPlayer = playerIdSessionIdsMap.get(removedPlayerId);
				if (currentSessionIdSetForPlayer != null) {
					currentSessionIdSetForPlayer.remove(id);
					if (currentSessionIdSetForPlayer.isEmpty()) {
						playerIdSessionIdsMap.remove(removedPlayerId);
					}
				}
			}
		}
		return removedSession;
	}

	public void addSession(WebSocketSession session) {
		idSessionMap.put(session.getId(), session);
		sessionIdActivetimeMap.put(session.getId(), System.currentTimeMillis());
	}

	public void bindPlayer(String sessionId, String playerId) {
		long bindTime = System.currentTimeMillis();
		logger.info("bindPlayer,bindTime:" + bindTime + ",playerId:" + playerId + ",sessionId:" + sessionId);
		Set<String> sessionAlreadyExistsIdSet = playerIdSessionIdsMap.get(playerId);
		sessionIdPlayerIdMap.put(sessionId, playerId);
		if (sessionAlreadyExistsIdSet == null) {
			sessionAlreadyExistsIdSet = new HashSet<>();
		}
		sessionAlreadyExistsIdSet.add(sessionId);
		playerIdSessionIdsMap.put(playerId, sessionAlreadyExistsIdSet);
		updateSession(sessionId);
	}

	public void updateSession(String id) {
		sessionIdActivetimeMap.put(id, System.currentTimeMillis());
	}

	public String findPlayerIdBySessionId(String sessionId) {
		return sessionIdPlayerIdMap.get(sessionId);
	}

	public void notifyToQuery(String playerId, List<QueryScope> scopes) {
		executorService.submit(() -> {
			for (QueryScope scope : scopes) {
				long notifyTime = System.currentTimeMillis();
				logger.info("notifyToQuery,notifyTime:" + notifyTime + ",playerId:" + playerId + ",scope:" + scope);
				Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
				if (sessionIdSet == null) {
					return;
				}
				CommonMO mo = new CommonMO();
				mo.setMsg("query");
				Map data = new HashMap();
				data.put("scope", scope.name());
				if (sessionIdSet.size() > 1) {
					data.put("tuoguan", true);
				} else {
					data.put("tuoguan", false);
				}
				mo.setData(data);
				String payLoad = gson.toJson(mo);
				sessionIdSet.forEach((sessionId) -> {
					WebSocketSession session = idSessionMap.get(sessionId);
					if (session != null) {
						sendMessage(session, payLoad);
					} else {

					}
				});
			}
		});
	}

	public void notifyToListenWisecrack(String playerId, String ordinal, String speakerId) {
		executorService.submit(() -> {
			Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
			if (sessionIdSet == null) {
				return;
			}
			CommonMO mo = new CommonMO();
			mo.setMsg("wisecrack");
			Map data = new HashMap();
			data.put("ordinal", ordinal);
			data.put("speakerId", speakerId);
			mo.setData(data);
			String payLoad = gson.toJson(mo);
			sessionIdSet.forEach((sessionId) -> {
				WebSocketSession session = idSessionMap.get(sessionId);
				if (session != null) {
					sendMessage(session, payLoad);
				} else {

				}
			});
		});
	}

	public void notifyToListenSpeak(String playerId, String wordId, String speakerId, boolean isPlayer) {
		executorService.submit(() -> {
			Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
			if (sessionIdSet == null) {
				return;
			}
			CommonMO mo = new CommonMO();
			mo.setMsg("speaking");
			Map data = new HashMap();
			data.put("wordId", wordId);
			data.put("speakerId", speakerId);
			data.put("isPlayer", isPlayer);
			mo.setData(data);
			String payLoad = gson.toJson(mo);
			sessionIdSet.forEach((sessionId) -> {
				WebSocketSession session = idSessionMap.get(sessionId);
				if (session != null) {
					sendMessage(session, payLoad);
				} else {

				}
			});
		});
	}

	/**
	 * 进入离开观战
	 * 
	 * @param key
	 *            input(进入) leave(离开)
	 * @param playerId
	 *            接收方id
	 */
	public void notifyWatchInfo(String playerId, String key, String id, String watcher, String headimgurl) {
		executorService.submit(() -> {
			Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
			if (sessionIdSet == null) {
				return;
			}
			CommonMO mo = new CommonMO();
			mo.setMsg("watcher");
			Map data = new HashMap();
			data.put("key", key);
			data.put("id", id);
			data.put("watcher", watcher);
			data.put("headimgurl", headimgurl);
			data.put("scope", "watcher");
			mo.setData(data);
			String payLoad = gson.toJson(mo);
			sessionIdSet.forEach((sessionId) -> {
				WebSocketSession session = idSessionMap.get(sessionId);
				if (session != null) {
					sendMessage(session, payLoad);
				} else {

				}
			});
		});
	}

	/**
	 * 通知观战者
	 */
	public void notifyToWatchQuery(List<String> playerIds, String flag) {
		executorService.submit(() -> {
			for (String playerId : playerIds) {
				Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
				if (sessionIdSet == null) {
					return;
				}
				for (WatchQueryScope list : WatchQueryScope.getQueryList(flag)) {
					CommonMO mo = new CommonMO();
					mo.setMsg("watch query");
					Map data = new HashMap();
					data.put("scope", list.name());
					mo.setData(data);
					String payLoad = gson.toJson(mo);
					sessionIdSet.forEach((sessionId) -> {
						WebSocketSession session = idSessionMap.get(sessionId);
						if (session != null) {
							sendMessage(session, payLoad);
						} else {

						}
					});
				}
			}
		});
	}

	private void sendMessage(WebSocketSession session, String message) {
		synchronized (session) {
			try {
				session.sendMessage(new TextMessage(message));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Scheduled(cron = "0/10 * * * * ?")
	public void closeOTSessions() {
		sessionIdActivetimeMap.forEach((id, time) -> {
			if ((System.currentTimeMillis() - time) > (30 * 1000)) {
				WebSocketSession sessionToClose = idSessionMap.get(id);
				if (sessionToClose != null) {
					try {
						sessionToClose.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	public void closeSessionForPlayer(String playerId) {
		Set<String> sessionIdSet = playerIdSessionIdsMap.get(playerId);
		if (sessionIdSet != null) {
			sessionIdSet.forEach((sessionId) -> {
				WebSocketSession session = idSessionMap.get(sessionId);
				if (session != null) {
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public boolean hasSessionForPlayer(String playerId) {
		return playerIdSessionIdsMap.containsKey(playerId);
	}

}
