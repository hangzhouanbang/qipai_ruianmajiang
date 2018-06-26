package com.anbang.qipai.ruianmajiang.cqrs.c.service;

public interface GameCmdService {

	void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu, Integer renshu,
			Boolean dapao);

	String leaveGame(String playerId) throws Exception;

	String readyForGame(String playerId, Long currentTime) throws Exception;

}
