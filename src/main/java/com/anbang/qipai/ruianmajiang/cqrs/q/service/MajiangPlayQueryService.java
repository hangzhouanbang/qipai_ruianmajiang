package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.majiang.LiangangangPanValueObjectPlayerViewFilter;
import com.dml.majiang.PanValueObject;
import com.dml.mpgame.GamePlayerState;
import com.dml.mpgame.GameState;

@Component
public class MajiangPlayQueryService {

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	@Autowired
	private MajiangGameDao majiangGameDao;

	private LiangangangPanValueObjectPlayerViewFilter pvFilter = new LiangangangPanValueObjectPlayerViewFilter();

	public PanValueObject findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
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
		PanValueObject panValueObject = new PanValueObject();
		try {
			panValueObject.fillByByteBuffer(ByteBuffer.wrap(frameData));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		pvFilter.filter(panValueObject, playerId);
		return panValueObject;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) {
		if (readyForGameResult.getGameState().equals(GameState.playing)) {
			majiangGameDao.update(readyForGameResult.getGameId(),
					readyForGameResult.getFirstActionframeDataOfFirstPan());
			// TODO 记录一条MajiangGameDbo，回放的时候要做
		}
	}

}
