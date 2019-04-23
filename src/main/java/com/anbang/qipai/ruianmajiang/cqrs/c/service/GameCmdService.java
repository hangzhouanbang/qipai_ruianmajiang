package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;

public interface GameCmdService {

	MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao);

	MajiangGameValueObject newMajiangGameLeaveAndQuit(String gameId, String playerId, Integer difen, Integer taishu,
			Integer panshu, Integer renshu, Boolean dapao);

	MajiangGameValueObject newMajiangGamePlayerLeaveAndQuit(String gameId, String playerId, Integer difen,
			Integer taishu, Integer panshu, Integer renshu, Boolean dapao);

	MajiangGameValueObject leaveGame(String playerId) throws Exception;

	ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception;

	ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception;

	MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception;

	void bindPlayer(String playerId, String gameId) throws Exception;

	MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception;

	MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception;

	MajiangGameValueObject finishGameImmediately(String gameId) throws Exception;

	MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception;

	MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception;

	MajiangGameValueObject joinWatch(String playerId, String nickName, String headimgurl, String gameId)
			throws Exception;

	MajiangGameValueObject leaveWatch(String playerId, String gameId) throws Exception;

	Map getwatch(String gameId);

	void recycleWatch(String gameId);

}
