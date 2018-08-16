package com.anbang.qipai.ruianmajiang.msg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.ruianmajiang.msg.channel.RuianMajiangGameSource;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;
import com.dml.majiang.pan.frame.PanValueObject;
import com.dml.mpgame.game.GamePlayer;
import com.dml.mpgame.game.GameValueObject;

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

	public void gameFinished(String gameId) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ju finished");
		Map data = new HashMap();
		data.put("gameId", gameId);
		mo.setData(data);
		ruianMajiangGameSource.ruianMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}

	public void panFinished(GameValueObject gameValueObject, PanValueObject panAfterAction) {
		CommonMO mo = new CommonMO();
		mo.setMsg("pan finished");
		Map data = new HashMap();
		data.put("gameId", gameValueObject.getId());
		data.put("no", panAfterAction.getNo());
		List<String> playerIds = new ArrayList<>();
		gameValueObject.getPlayers().forEach((gamePlayer) -> playerIds.add(gamePlayer.getId()));
		data.put("playerIds", playerIds);
		mo.setData(data);
		ruianMajiangGameSource.ruianMajiangGame().send(MessageBuilder.withPayload(mo).build());
	}
}
