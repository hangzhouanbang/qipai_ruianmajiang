package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
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

		if (majiangActionResult.getJuResult() != null) {// 全部结束
			GameValueObject gameValueObject = gameServer.finish(gameId);
			majiangActionResult.setGameValueObject(gameValueObject);
		}
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
		List<String> allPlayerIds = gameServer.findAllPlayerIdsForGame(gameId);
		allPlayerIds.remove(playerId);
		readyToNextPanResult.setOtherPlayerIds(allPlayerIds);
		return readyToNextPanResult;
	}

}
