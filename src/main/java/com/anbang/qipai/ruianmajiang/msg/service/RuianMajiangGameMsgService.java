package com.anbang.qipai.ruianmajiang.msg.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.ruianmajiang.msg.channel.RuianMajiangGameSource;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;
import com.dml.mpgame.GamePlayer;
import com.dml.mpgame.GameValueObject;

@EnableBinding(RuianMajiangGameSource.class)
public class RuianMajiangGameMsgService {

	@Autowired
	private RuianMajiangGameSource ruianMajiangGameSource;

	public void gamePlayerLeave(GameValueObject gameValueObject, String playerId) {
		boolean playerIsQuit = true;
		for (GamePlayer gamePlayer : gameValueObject.getPlayers()) {
			if (gamePlayer.getId().equals(playerId)) {
				playerIsQuit = false;
				break;
			}
		}
		if (playerIsQuit) {
			CommonMO mo = new CommonMO();
			mo.setMsg("playerQuit");
			Map data = new HashMap();
			data.put("gameId", gameValueObject.getId());
			data.put("playerId", playerId);
			mo.setData(data);
			ruianMajiangGameSource.ruianMajiangGame().send(MessageBuilder.withPayload(mo).build());
		}
	}

}
