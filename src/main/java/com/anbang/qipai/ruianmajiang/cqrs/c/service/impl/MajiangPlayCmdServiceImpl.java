package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGame;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.MajiangPlayCmdService;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.player.PlayerNotInGameException;
import com.dml.mpgame.server.GameServer;

@Component
public class MajiangPlayCmdServiceImpl extends CmdServiceBase implements MajiangPlayCmdService {

	@Override
	public MajiangActionResult action(String playerId, Integer actionId, Long actionTime) throws Exception {

		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		String gameId = gameServer.findBindGameId(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}

		MajiangGame majiangGame = (MajiangGame) gameServer.findGame(gameId);
		MajiangActionResult majiangActionResult = majiangGame.action(playerId, actionId, actionTime);

		if (majiangActionResult.getJuResult() != null) {// 全部结束
			gameServer.finishGameImmediately(gameId);
		}

		return majiangActionResult;

	}

	@Override
	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {

		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		String gameId = gameServer.findBindGameId(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		MajiangGame majiangGame = (MajiangGame) gameServer.findGame(gameId);

		ReadyToNextPanResult readyToNextPanResult = new ReadyToNextPanResult();
		majiangGame.readyToNextPan(playerId);
		PanActionFrame firstActionFrame = majiangGame.getJu().getCurrentPan().findLatestActionFrame();
		readyToNextPanResult.setFirstActionFrame(firstActionFrame);
		readyToNextPanResult.setMajiangGame(new MajiangGameValueObject(majiangGame));
		return readyToNextPanResult;

	}

}
