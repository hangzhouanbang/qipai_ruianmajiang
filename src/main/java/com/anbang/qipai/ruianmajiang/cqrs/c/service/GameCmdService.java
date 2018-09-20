package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;

public interface GameCmdService {

	MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao);

	MajiangGameValueObject leaveGame(String playerId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

	MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

	void bindPlayer(String playerId, String gameId);

	MajiangGameValueObject finish(String playerId) throws Exception;

	MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

	MajiangGameValueObject finishGameImmediately(String gameId) throws Exception;
}
