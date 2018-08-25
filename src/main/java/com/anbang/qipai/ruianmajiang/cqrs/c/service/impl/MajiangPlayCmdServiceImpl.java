package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.MajiangPlayCmdService;
import com.dml.mpgame.game.GameValueObject;
import com.dml.mpgame.game.PlayerNotInGameException;
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

		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		MajiangActionResult majiangActionResult = majiangGameManager.majiangAction(playerId, gameId, actionId,
				actionTime);

		GameValueObject gameValueObject;
		if (majiangActionResult.getJuResult() != null) {// 全部结束
			gameValueObject = gameServer.finishGameImmediately(gameId);
		} else {
			gameValueObject = gameServer.findGame(gameId);
		}

		MajiangGameValueObject majiangGameValueObject = majiangGameManager.updateMajiangGameByGame(gameValueObject);
		majiangActionResult.setMajiangGame(majiangGameValueObject);

		return majiangActionResult;

	}

	@Override
	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {

		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		GameServer gameServer = singletonEntityRepository.getEntity(GameServer.class);
		String gameId = gameServer.findBindGameId(playerId);
		if (gameId == null) {
			throw new PlayerNotInGameException();
		}
		ReadyToNextPanResult readyToNextPanResult = majiangGameManager.readyToNextPan(playerId, gameId);
		return readyToNextPanResult;
	}

}
