package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.VoteToFinishResult;
import com.dml.mpgame.game.GameValueObject;

public interface GameCmdService {

	void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu, Integer renshu,
			Boolean dapao);

	GameValueObject leaveGame(String playerId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	GameValueObject joinGame(String playerId, String gameId) throws Exception;

	GameValueObject backToGame(String playerId, String gameId) throws Exception;

	void bindPlayer(String playerId, String gameId);

	VoteToFinishResult launchFinishVote(String playerId) throws Exception;

	RuianMajiangJuResult finishGame(String gameId) throws Exception;

}
