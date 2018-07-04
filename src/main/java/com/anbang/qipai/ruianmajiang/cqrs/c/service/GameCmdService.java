package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.JoinGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;

public interface GameCmdService {

	void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu, Integer renshu,
			Boolean dapao);

	String leaveGame(String playerId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	JoinGameResult joinGame(String playerId, String gameId) throws Exception;

}
