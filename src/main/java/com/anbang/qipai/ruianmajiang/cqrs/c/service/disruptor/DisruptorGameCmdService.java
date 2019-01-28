package com.anbang.qipai.ruianmajiang.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.impl.GameCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "gameCmdService")
public class DisruptorGameCmdService extends DisruptorCmdServiceBase implements GameCmdService {

	@Autowired
	private GameCmdServiceImpl gameCmdServiceImpl;

	@Override
	public MajiangGameValueObject newMajiangGame(String gameId, String playerId, Integer difen, Integer taishu,
			Integer panshu, Integer renshu, Boolean dapao) {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGame", gameId, playerId,
				difen, taishu, panshu, renshu, dapao);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.newMajiangGame(
							cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
							cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MajiangGameValueObject newMajiangGameForXiuxianchang(String gameId, String playerId, Integer difen,
			Integer taishu, Integer panshu, Integer renshu, Boolean dapao) {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "newMajiangGameForXiuxianchang",
				gameId, playerId, difen, taishu, panshu, renshu, dapao);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.newMajiangGameForXiuxianchang(
							cmd.getParameter(), cmd.getParameter(), cmd.getParameter(), cmd.getParameter(),
							cmd.getParameter(), cmd.getParameter(), cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MajiangGameValueObject joinGame(String playerId, String gameId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "joinGame", playerId, gameId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.joinGame(cmd.getParameter(),
							cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject leaveGame(String playerId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGame", playerId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.leaveGame(cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ReadyForGameResult readyForGame(String playerId, Long currentTime) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "readyForGame", playerId,
				currentTime);
		DeferredResult<ReadyForGameResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			ReadyForGameResult readyForGameResult = gameCmdServiceImpl.readyForGame(cmd.getParameter(),
					cmd.getParameter());
			return readyForGameResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject backToGame(String playerId, String gameId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "backToGame", playerId, gameId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.backToGame(cmd.getParameter(),
							cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void bindPlayer(String playerId, String gameId) {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "bindPlayer", playerId, gameId);
		DeferredResult<Object> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			gameCmdServiceImpl.bindPlayer(cmd.getParameter(), cmd.getParameter());
			return null;
		});
		try {
			result.getResult();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public MajiangGameValueObject finish(String playerId, Long currentTime) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "finish", playerId, currentTime);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.finish(cmd.getParameter(),
							cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject voteToFinish(String playerId, Boolean yes) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "voteToFinish", playerId, yes);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl.voteToFinish(cmd.getParameter(),
							cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject finishGameImmediately(String gameId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "finishGameImmediately", gameId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
							.finishGameImmediately(cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject leaveGameByOffline(String playerId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGameByOffline", playerId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
							.leaveGameByOffline(cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject leaveGameByHangup(String playerId) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "leaveGameByHangup", playerId);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
							.leaveGameByHangup(cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MajiangGameValueObject voteToFinishByTimeOver(String playerId, Long currentTime) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "voteToFinishByTimeOver", playerId,
				currentTime);
		DeferredResult<MajiangGameValueObject> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd,
				() -> {
					MajiangGameValueObject majiangGameValueObject = gameCmdServiceImpl
							.voteToFinishByTimeOver(cmd.getParameter(), cmd.getParameter());
					return majiangGameValueObject;
				});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ReadyForGameResult cancelReadyForGame(String playerId, Long currentTime) throws Exception {
		CommonCommand cmd = new CommonCommand(GameCmdServiceImpl.class.getName(), "cancelReadyForGame", playerId,
				currentTime);
		DeferredResult<ReadyForGameResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			ReadyForGameResult readyForGameResult = gameCmdServiceImpl.cancelReadyForGame(cmd.getParameter(),
					cmd.getParameter());
			return readyForGameResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
