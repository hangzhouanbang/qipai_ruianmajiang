package com.anbang.qipai.ruianmajiang.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.ruianmajiang.web.vo.CommonVO;
import com.anbang.qipai.ruianmajiang.web.vo.GameVO;

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
	private MajiangGameQueryService majiangGameQueryService;

	@Autowired
	private PlayerAuthService playerAuthService;

	/**
	 * 新一局游戏
	 */
	@RequestMapping(value = "/newgame")
	@ResponseBody
	public CommonVO newgame(String playerId, int difen, int taishu, int panshu, int renshu, boolean dapao) {
		CommonVO vo = new CommonVO();
		String newGameId = UUID.randomUUID().toString();
		gameCmdService.newMajiangGame(newGameId, playerId, difen, taishu, panshu, renshu, dapao);
		majiangGameQueryService.newMajiangGame(newGameId, playerId, difen, taishu, panshu, renshu, dapao);
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("gameId", newGameId);
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 加入游戏
	 */
	@RequestMapping(value = "/joingame")
	@ResponseBody
	public CommonVO joingame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		try {
			gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);
		vo.setData(data);
		return vo;
	}

	/**
	 * 游戏的所有信息,不包含局
	 * 
	 * @param gameId
	 * @return
	 */
	@RequestMapping(value = "/info")
	@ResponseBody
	public CommonVO info(String gameId) {
		CommonVO vo = new CommonVO();
		MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
		List<GamePlayerDbo> gamePlayerDboListForGameId = majiangGameQueryService.findGamePlayerDbosForGame(gameId);
		GameVO gameVO = new GameVO(majiangGameDbo, gamePlayerDboListForGameId);
		Map data = new HashMap();
		data.put("game", gameVO);
		vo.setData(data);
		return vo;
	}

	/**
	 * 最开始的准备,不适用下一盘的准备
	 * 
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/ready")
	@ResponseBody
	public CommonVO ready(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		String gameId = gameCmdService.readyForGame(playerId, System.currentTimeMillis());
	}

}
