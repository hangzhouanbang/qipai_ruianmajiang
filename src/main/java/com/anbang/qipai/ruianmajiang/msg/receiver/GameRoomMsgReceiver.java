package com.anbang.qipai.ruianmajiang.msg.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.ruianmajiang.msg.channel.GameRoomSink;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.ruianmajiang.msg.msjobj.MajiangHistoricalJuResult;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangGameMsgService;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangResultMsgService;
import com.dml.mpgame.game.player.GamePlayerOnlineState;
import com.google.gson.Gson;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private RuianMajiangResultMsgService ruianMajiangResultMsgService;

	@Autowired
	private RuianMajiangGameMsgService ruianMajiangGameMsgService;

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.RUIANGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {
			List<String> gameIds = gson.fromJson(json, ArrayList.class);
			for (String gameId : gameIds) {
				try {
					MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
					boolean playerOnline = false;
					for (MajiangGamePlayerDbo player : majiangGameDbo.getPlayers()) {
						if (GamePlayerOnlineState.online.equals(player.getOnlineState())) {
							playerOnline = true;
						}
					}
					if (playerOnline) {
						ruianMajiangGameMsgService.delay(gameId);
					} else {
						MajiangGameValueObject gameValueObject = gameCmdService.finishGameImmediately(gameId);
						majiangGameQueryService.finishGameImmediately(gameValueObject);
						JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
						MajiangHistoricalJuResult juResult = new MajiangHistoricalJuResult(juResultDbo, majiangGameDbo);
						ruianMajiangResultMsgService.recordJuResult(juResult);
						ruianMajiangGameMsgService.gameFinished(gameId);
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
	}

}
