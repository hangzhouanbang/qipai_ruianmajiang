package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerState;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameState;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangJuResultMsgService;
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

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private RuianMajiangJuResultMsgService ruianMajiangJuResultMsgService;

	private LiangangangPanActionFramePlayerViewFilter pvFilter = new LiangangangPanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		MajiangGamePlayerDbo gamePlayerDbo = gamePlayerDboDao.findByPlayerIdAndGameId(playerId, gameId);
		if (gamePlayerDbo == null) {
			throw new Exception("player dont play game");
		}

		if (!gamePlayerDbo.getState().equals(MajiangGamePlayerState.playing)) {
			throw new Exception("player dont play game");
		}

		MajiangGameDbo majiangGameDbo = majiangGameDao.findById(gamePlayerDbo.getGameId());
		if (!majiangGameDbo.getState().equals(MajiangGameState.playing)) {
			throw new Exception("game not playing");
		}
		byte[] frameData = majiangGameDbo.getLatestPanActionFrameData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		GameValueObject gameValueObject = readyForGameResult.getGame();
		gameValueObject.getPlayers().forEach((player) -> {
			if (player.getState().equals(GamePlayerState.readyToStart)) {
				gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), MajiangGamePlayerState.readyToStart);
			}
		});
		if (gameValueObject.getState().equals(GameState.playing)) {
			majiangGameDao.update(gameValueObject.getId(), MajiangGameState.playing);
			gamePlayerDboDao.updatePlayersStateForGame(gameValueObject.getId(), MajiangGamePlayerState.playing);
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			majiangGameDao.update(gameValueObject.getId(), panActionFrame.toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		}
	}

	public void readyToNextPan(String playerId, ReadyToNextPanResult readyToNextPanResult) throws Throwable {

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			gamePlayerDboDao.updatePlayersStateForGame(readyToNextPanResult.getGameId(),
					MajiangGamePlayerState.playing);
			majiangGameDao.update(readyToNextPanResult.getGameId(), MajiangGameState.playing);
			majiangGameDao.update(readyToNextPanResult.getGameId(),
					readyToNextPanResult.getFirstActionFrame().toByteArray(1024 * 8));
			// TODO 记录一条Frame，回放的时候要做
		} else {
			gamePlayerDboDao.update(playerId, readyToNextPanResult.getGameId(), MajiangGamePlayerState.readyToStart);
		}

	}

	public void action(MajiangActionResult majiangActionResult) throws Throwable {
		String gameId = majiangActionResult.getGameValueObject().getId();
		PanActionFrame panActionFrame = majiangActionResult.getPanActionFrame();
		majiangGameDao.update(gameId, panActionFrame.toByteArray(1024 * 8));

		// 盘出结果的话要记录结果
		RuianMajiangPanResult ruianMajiangPanResult = majiangActionResult.getPanResult();
		if (ruianMajiangPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, ruianMajiangPanResult);
			panResultDboDao.save(panResultDbo);
			// 所有人的状态要改变
			if (majiangActionResult.getJuResult() == null) {// 局没结束，还有下一盘
				gamePlayerDboDao.updatePlayersStateForGame(gameId, MajiangGamePlayerState.panFinished);
				majiangGameDao.update(gameId, MajiangGameState.waitingNextPan);
			} else {// 一切都结束了
				// 要记录局结果 TODO
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, majiangActionResult.getJuResult());
				juResultDboDao.save(juResultDbo);
				ruianMajiangJuResultMsgService.recordJuResult(juResultDbo);
				majiangGameDao.update(gameId, MajiangGameState.finished);
				gamePlayerDboDao.updatePlayersStateForGame(gameId, MajiangGamePlayerState.finished);
			}

			// 更新每个玩家的总分
			for (RuianMajiangPanPlayerResult panPlayerResult : ruianMajiangPanResult.getPlayerResultList()) {
				gamePlayerDboDao.updateTotalScore(gameId, panPlayerResult.getPlayerId(),
						panPlayerResult.getTotalScore());
			}

		}

		// TODO 记录一条Frame，回放的时候要做
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) {
		return panResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public Map<String, MajiangGamePlayerDbo> findGamePlayersAsMap(String gameId) {
		List<MajiangGamePlayerDbo> gamePlayerDboList = gamePlayerDboDao.findByGameId(gameId);
		Map<String, MajiangGamePlayerDbo> map = new HashMap<>();
		for (MajiangGamePlayerDbo gamePlayerDbo : gamePlayerDboList) {
			map.put(gamePlayerDbo.getPlayerId(), gamePlayerDbo);
		}
		return map;
	}

	public JuResultDbo findJuResultDbo(String gameId) {
		return juResultDboDao.findByGameId(gameId);
	}

}
