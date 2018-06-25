package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.PlayerNotInGameException;
import com.dml.mpgame.GamePlayerNotFoundException;

public interface GameCmdService {

	void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu, Integer renshu,
			Boolean dapao);

	String leaveGame(String playerId) throws PlayerNotInGameException, GamePlayerNotFoundException;

}
