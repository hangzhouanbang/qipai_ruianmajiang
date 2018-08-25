package com.anbang.qipai.ruianmajiang.msg.receiver;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.ruianmajiang.msg.channel.GameRoomSink;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;
import com.dml.mpgame.game.GameValueObject;
import com.google.gson.Gson;

@EnableBinding(GameRoomSink.class)
public class GameRoomMsgReceiver {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private MajiangGameQueryService majiangGameQueryService;

	private Gson gson = new Gson();

	@StreamListener(GameRoomSink.RUIANGAMEROOM)
	public void removeGameRoom(CommonMO mo) {
		String msg = mo.getMsg();
		String json = gson.toJson(mo.getData());
		if ("gameIds".equals(msg)) {
			List<String> gameIds = gson.fromJson(json, ArrayList.class);
			for (String gameId : gameIds) {
				GameValueObject gameValueObject;
				try {
					gameValueObject = gameCmdService.finishGameImmediately(gameId);
				} catch (Exception e) {
				}
			}
		}
	}
}
