package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.PlayerNotInGameException;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.dml.mpgame.GamePlayerNotFoundException;

@Component
public class GameCmdServiceImpl extends CmdServiceBase implements GameCmdService {

	@Override
	public void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao) {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		majiangGameManager.newMajiangGame(gameId, playerId, difen, taishu, panshu, renshu, dapao);
	}

	@Override
	public String leaveGame(String playerId) throws PlayerNotInGameException, GamePlayerNotFoundException {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		return majiangGameManager.leave(playerId);
	}

}
