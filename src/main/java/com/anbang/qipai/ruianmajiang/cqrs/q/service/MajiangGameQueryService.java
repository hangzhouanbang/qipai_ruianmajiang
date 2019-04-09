package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GameFinishVoteDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.WatchRecordDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedGameFinishVoteDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedJuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedMajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteValueObjectDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.watch.WatchRecord;
import com.dml.mpgame.game.watch.Watcher;

@Component
public class MajiangGameQueryService {

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private GameFinishVoteDboDao gameFinishVoteDboDao;

	@Autowired
	private MemcachedMajiangGameDboDao memcachedMajiangGameDboDao;

	@Autowired
	private MemcachedGameFinishVoteDboDao memcachedGameFinishVoteDboDao;

	@Autowired
	private MemcachedJuResultDboDao memcachedJuResultDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private WatchRecordDao watchRecordDao;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	public MajiangGameDbo findMajiangGameDboById(String gameId) throws Exception {
		return memcachedMajiangGameDboDao.findById(gameId);
	}

	public void newMajiangGame(MajiangGameValueObject majiangGame) throws Throwable {

		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

	}

	public void backToGame(String playerId, MajiangGameValueObject majiangGameValueObject) throws Throwable {
		memcachedMajiangGameDboDao.updatePlayerOnlineState(majiangGameValueObject.getId(), playerId,
				majiangGameValueObject.findPlayerOnlineState(playerId));
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			memcachedGameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
		}
	}

	public void joinGame(MajiangGameValueObject majiangGame) throws Throwable {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);
	}

	public void leaveGame(MajiangGameValueObject majiangGame) throws Throwable {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGame.getVote();
		if (gameFinishVoteValueObject != null) {
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(new GameFinishVoteValueObjectDbo(gameFinishVoteValueObject));
			gameFinishVoteDbo.setGameId(majiangGame.getId());
			memcachedGameFinishVoteDboDao.save(gameFinishVoteDbo);
		}

		if (majiangGame.getJuResult() != null) {
			RuianMajiangJuResult ruianMajiangJuResult = (RuianMajiangJuResult) majiangGame.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGame.getId(), null, ruianMajiangJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				majiangGameDboDao.save(majiangGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao
							.findByGameId(majiangGame.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void finishGameImmediately(MajiangGameValueObject majiangGameValueObject) throws Throwable {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			RuianMajiangJuResult ruianMajiangJuResult = (RuianMajiangJuResult) majiangGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, ruianMajiangJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				majiangGameDboDao.save(majiangGameDbo);
				juResultDboDao.save(juResultDbo);
			});
		}
	}

	public void finish(MajiangGameValueObject majiangGameValueObject) throws Throwable {
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			GameFinishVoteDbo gameFinishVoteDbo = new GameFinishVoteDbo();
			gameFinishVoteDbo.setVote(new GameFinishVoteValueObjectDbo(gameFinishVoteValueObject));
			gameFinishVoteDbo.setGameId(majiangGameValueObject.getId());
			memcachedGameFinishVoteDboDao.save(gameFinishVoteDbo);
		}
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			RuianMajiangJuResult ruianMajiangJuResult = (RuianMajiangJuResult) majiangGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, ruianMajiangJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				majiangGameDboDao.save(majiangGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao
							.findByGameId(majiangGameValueObject.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public void voteToFinish(MajiangGameValueObject majiangGameValueObject) throws Throwable {
		GameFinishVoteValueObject gameFinishVoteValueObject = majiangGameValueObject.getVote();
		if (gameFinishVoteValueObject != null) {
			memcachedGameFinishVoteDboDao.update(majiangGameValueObject.getId(), gameFinishVoteValueObject);
		}
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGameValueObject.allPlayerIds()
				.forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGameValueObject, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		if (majiangGameValueObject.getJuResult() != null) {
			RuianMajiangJuResult ruianMajiangJuResult = (RuianMajiangJuResult) majiangGameValueObject.getJuResult();
			JuResultDbo juResultDbo = new JuResultDbo(majiangGameValueObject.getId(), null, ruianMajiangJuResult);
			memcachedJuResultDboDao.save(juResultDbo);
			executorService.submit(() -> {
				majiangGameDboDao.save(majiangGameDbo);
				juResultDboDao.save(juResultDbo);
				try {
					GameFinishVoteDbo gameFinishVoteDbo = memcachedGameFinishVoteDboDao
							.findByGameId(majiangGameValueObject.getId());
					if (gameFinishVoteDbo != null) {
						gameFinishVoteDboDao.save(gameFinishVoteDbo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public GameFinishVoteDbo findGameFinishVoteDbo(String gameId) throws Exception {
		return memcachedGameFinishVoteDboDao.findByGameId(gameId);
	}

	public WatchRecord saveWatchRecord(String gameId, Watcher watcher) {
		WatchRecord watchRecord = watchRecordDao.findByGameId(gameId);
		if (watchRecord == null) {
			WatchRecord record = new WatchRecord();
			List<Watcher> watchers = new ArrayList<>();
			watchers.add(watcher);

			record.setGameId(gameId);
			record.setWatchers(watchers);
			watchRecordDao.save(record);
			return record;
		}

		for (Watcher list : watchRecord.getWatchers()) {
			if (list.getId().equals(watcher.getId())) {
				list.setState(watcher.getState());
				watchRecordDao.save(watchRecord);
				return watchRecord;
			}
		}

		watchRecord.getWatchers().add(watcher);
		watchRecordDao.save(watchRecord);
		return watchRecord;
	}

	/**
	 * 查询观战中的玩家
	 */
	public boolean findByPlayerId(String gameId, String playerId) {
		if (watchRecordDao.findByPlayerId(gameId, playerId, "join") != null) {
			return true;
		}
		return false;
	}

}
