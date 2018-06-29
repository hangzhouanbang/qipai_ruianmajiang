package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao) {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		majiangGameManager.newMajiangGame(gameId, playerId, difen, taishu, panshu, renshu, dapao);
	}

	@Override
	public String leaveGame(String playerId) throws Exception {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		return majiangGameManager.leave(playerId);
	}

	@Override
	public String readyForGame(String playerId, Long currentTime) throws Exception {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		return majiangGameManager.ready(playerId, currentTime);
	}

	@Override
	public void joinGame(String playerId, String gameId) throws Exception {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		majiangGameManager.join(playerId, gameId);
	}

}
