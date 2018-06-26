package com.anbang.qipai.ruianmajiang.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.impl.GameCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "gameCmdService")
public class DisruptorGameCmdService extends DisruptorCmdServiceBase implements GameCmdService {

	@Autowired
	private GameCmdServiceImpl gameCmdServiceImpl;

	@Override
	public void newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu, Integer panshu,
			Integer renshu, Boolean dapao) {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGame", gameId, playerId,
				difen, taishu, panshu, renshu, dapao);
		DeferredResult<Object> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			gameCmdServiceImpl.newMajiangGame(cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
					cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
			return null;
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String leaveGame(String playerId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGame", playerId);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String gameId = gameCmdServiceImpl.leaveGame(cmd.getParameter());
			return gameId;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String readyForGame(String playerId, Long currentTime) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "readyForGame", playerId,
				currentTime);
		DeferredResult<String> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			String gameId = gameCmdServiceImpl.readyForGame(cmd.getParameter(), cmd.getParameter());
			return gameId;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
