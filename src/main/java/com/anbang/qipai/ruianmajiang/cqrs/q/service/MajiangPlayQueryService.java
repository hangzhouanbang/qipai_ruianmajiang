package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.dml.majiang.ByteBufferSerializer;
import com.dml.majiang.LiangangangPanActionFramePlayerViewFilter;
import com.dml.majiang.PanActionFrame;
import com.dml.mpgame.GamePlayerState;
import com.dml.mpgame.GameState;
import com.dml.mpgame.GameValueObject;

@Component
public class MajiangPlayQueryService {

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	@Autowired
	private MajiangGameDao majiangGameDao;

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
		PanActionFrame panActionFrame = null;
		try {
			panActionFrame = ByteBufferSerializer.byteBufferToObj(ByteBuffer.wrap(frameData));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) {
		GameValueObject gameValueObject = readyForGameResult.getGame();
		majiangGameDao.update(gameValueObject.getId(), gameValueObject.getState());
		gameValueObject.getPlayers().forEach((player) -> {
			gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), player.getState());
		});
		if (gameValueObject.getState().equals(GameState.playing)) {
			majiangGameDao.update(gameValueObject.getId(), readyForGameResult.getFirstActionframeDataOfFirstPan());
			// TODO 记录一条MajiangGameDbo，回放的时候要做
		}
	}

	public void action(MajiangActionResult majiangActionResult) {
		GameValueObject gameValueObject = majiangActionResult.getGame();
		majiangGameDao.update(gameValueObject.getId(), gameValueObject.getState());
		gameValueObject.getPlayers().forEach((player) -> {
			gamePlayerDboDao.update(player.getId(), gameValueObject.getId(), player.getState());
		});

		majiangGameDao.update(gameValueObject.getId(), majiangActionResult.getActionFrameDataAfterAction());
		// TODO 记录一条MajiangGameDbo，回放的时候要做
	}

}
