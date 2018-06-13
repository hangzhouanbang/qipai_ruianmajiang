package com.anbang.qipai.ruianmajiang.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.ruianmajiang.web.vo.CommonVO;

/**
 * 游戏框架相关
 * 
 * @author neo
 *
 */
@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	private GameCmdService gameCmdService;

	@Autowired
	private PlayerAuthService playerAuthService;

	/**
	 * 新一局游戏
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/newgame")
	@ResponseBody
	public CommonVO newgame(String playerId, int difen, int taishu, int panshu, int renshu, boolean dapao) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		gameCmdService.newMajiangGame(newGameId, playerId, difen, taishu, panshu, renshu, dapao);
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

}
