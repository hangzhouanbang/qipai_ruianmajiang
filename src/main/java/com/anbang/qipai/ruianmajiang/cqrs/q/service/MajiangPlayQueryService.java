package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;
import com.dml.majiang.pan.frame.LiangangangPanActionFramePlayerViewFilter;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.GamePlayerState;
import com.dml.mpgame.GameState;
import com.dml.mpgame.GameValueObject;

@Component
public class MajiangPlayQueryService {

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	@Autowired
	private MajiangGameDboDao majiangGameDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	private LiangangangPanActionFramePlayerViewFilter pvFilter = new LiangangangPanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		GamePlayerDbo gamePlayerDbo = gamePlayerDboDao.findByPlayerIdAndGameId(playerId, gameId);
		if (gamePlayerDbo == null) {
			throw new Exception("player dont play game");
		}

		if (!gamePlayerDbo.getState().equals(GamePlayerState.playing)) {
			throw new Exception("player dont play game");
		}

		MajiangGameDbo majiangGameDbo = majiangGameDao.findById(gamePlayerDbo.getGameId());
		if (!majiangGameDbo.getState().equals(GameState.playing)) {
			throw new Exception("game not playing");
		}
		byte[] frameData = majiangGameDbo.getLatestPanActionFrameData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		GameValueObject gameValueObject = readyForGameResult.getGame();
		majiangGameDao.update(gameValueObject.getId(), gameValueObject.getState());
		gameValueObject.getPlayers().forEach((player) -> {
			gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), player.getState());
		});
		if (gameValueObject.getState().equals(GameState.playing)) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			majiangGameDao.update(gameValueObject.getId(), panActionFrame.toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		}
	}

	public void action(MajiangActionResult majiangActionResult) throws Throwable {
		GameValueObject gameValueObject = majiangActionResult.getGame();
		majiangGameDao.update(gameValueObject.getId(), gameValueObject.getState());
		gameValueObject.getPlayers().forEach((player) -> {
			gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), player.getState());
		});

		PanActionFrame panActionFrame = majiangActionResult.getPanActionFrame();
		majiangGameDao.update(gameValueObject.getId(), panActionFrame.toByteArray(1024 * 8));

		// 盘出结果的话要记录结果
		RuianMajiangPanResult ruianMajiangPanResult = majiangActionResult.getPanResult();
		if (ruianMajiangPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameValueObject.getId(), ruianMajiangPanResult);
			panResultDboDao.save(panResultDbo);
		}

		// TODO 记录一条Frame，回放的时候要做
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) {
		return panResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public Map<String, GamePlayerDbo> findGamePlayersAsMap(String gameId) {
		List<GamePlayerDbo> gamePlayerDboList = gamePlayerDboDao.findByGameId(gameId);
		Map<String, GamePlayerDbo> map = new HashMap<>();
		for (GamePlayerDbo gamePlayerDbo : gamePlayerDboList) {
			map.put(gamePlayerDbo.getPlayerId(), gamePlayerDbo);
		}
		return map;
	}

}
