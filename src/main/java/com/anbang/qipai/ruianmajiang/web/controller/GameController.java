package com.anbang.qipai.ruianmajiang.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.FinishResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameState;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangGameMsgService;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangResultMsgService;
import com.anbang.qipai.ruianmajiang.web.vo.CommonVO;
import com.anbang.qipai.ruianmajiang.web.vo.GameVO;
import com.anbang.qipai.ruianmajiang.web.vo.JuResultVO;
import com.anbang.qipai.ruianmajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.ruianmajiang.websocket.QueryScope;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.GameValueObject;

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

	@Autowired
	private MajiangPlayQueryService majiangPlayQueryService;

	@Autowired
	private GamePlayWsNotifier wsNotifier;

	@Autowired
	private RuianMajiangGameMsgService gameMsgService;

	@Autowired
	private RuianMajiangResultMsgService ruianMajiangResultMsgService;

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
		GameValueObject gameValueObject;
		try {
			gameValueObject = gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		majiangGameQueryService.joinGame(playerId, gameId);
		// 通知其他人
		for (String otherPlayerId : gameValueObject.allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		}

		String token = playerAuthService.newSessionForPlayer(playerId);
		Map data = new HashMap();
		data.put("token", token);

		vo.setData(data);
		return vo;
	}

	/**
	 * 离开游戏(非退出,还会回来的)
	 */
	@RequestMapping(value = "/leavegame")
	@ResponseBody
	public CommonVO leavegame(String token) {
		CommonVO vo = new CommonVO();
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}
		// 断开玩家的socket
		wsNotifier.closeSessionForPlayer(playerId);
		GameValueObject gameValueObject;
		try {
			gameValueObject = gameCmdService.leaveGame(playerId);
			if (gameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.leaveGame(gameValueObject);
		gameMsgService.gamePlayerLeave(gameValueObject, playerId);
		// 通知其他玩家
		List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService
				.findGamePlayerDbosForGame(gameValueObject.getId());
		gamePlayerDboList.forEach((gamePlayerDbo) -> {
			String otherPlayerId = gamePlayerDbo.getPlayerId();
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		});
		return vo;
	}

	/**
	 * 返回游戏
	 */
	@RequestMapping(value = "/backtogame")
	@ResponseBody
	public CommonVO backtogame(String playerId, String gameId) {
		CommonVO vo = new CommonVO();
		try {
			gameCmdService.backToGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}

		majiangGameQueryService.backToGame(playerId, gameId);

		// 通知其他玩家
		List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService.findGamePlayerDbosForGame(gameId);
		gamePlayerDboList.forEach((gamePlayerDbo) -> {
			String otherPlayerId = gamePlayerDbo.getPlayerId();
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			}
		});

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
		//防止游戏结束却没解散房间
		if(MajiangGameState.finished.equals(majiangGameDbo.getState())) {
			gameMsgService.gameFinished(majiangGameDbo.getId());
		}
		List<MajiangGamePlayerDbo> gamePlayerDboListForGameId = majiangGameQueryService
				.findGamePlayerDbosForGame(gameId);
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

		ReadyForGameResult readyForGameResult;
		try {
			readyForGameResult = gameCmdService.readyForGame(playerId, System.currentTimeMillis());
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}

		try {
			majiangPlayQueryService.readyForGame(readyForGameResult);// TODO 一起点准备的时候可能有同步问题.要靠框架解决
		} catch (Throwable e) {
			vo.setSuccess(false);
			vo.setMsg(e.getMessage());
			return vo;
		}
		// 通知其他人
		for (String otherPlayerId : readyForGameResult.getOtherPlayerIds()) {
			wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
			if (readyForGameResult.getGame().getState().equals(GameState.playing)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.panForMe.name());
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (readyForGameResult.getGame().getState().equals(GameState.playing)) {
			queryScopes.add(QueryScope.panForMe);
		}
		data.put("queryScopes", queryScopes);
		return vo;
	}

	@RequestMapping(value = "/finish")
	@ResponseBody
	public CommonVO finish(String token) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		FinishResult finishResult;
		try {
			finishResult = gameCmdService.finish(playerId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.finish(finishResult);
		GameValueObject gameValueObject = finishResult.getGameValueObject();
		String gameId = gameValueObject.getId();
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			Map<String, MajiangGamePlayerDbo> playerMap = majiangPlayQueryService.findGamePlayersAsMap(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, playerMap, majiangGameDbo.getPanshu());
			ruianMajiangResultMsgService.recordJuResult(juResult);
			gameMsgService.gameFinished(gameId);
		}

		if (finishResult.getGameValueObject().getState().equals(GameState.finished)) {
			data.put("queryScope", QueryScope.gameInfo);
			// 通知其他人来查询
			List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService.findGamePlayerDbosForGame(gameId);
			gamePlayerDboList.forEach((gamePlayerDbo) -> {
				String otherPlayerId = gamePlayerDbo.getPlayerId();
				if (!otherPlayerId.equals(playerId)) {
					wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
				}
			});
		} else {
			// 游戏没结束有两种可能：一种是发起了投票。还有一种是游戏没开始，解散发起人又不是房主，那就自己走人。
			if (gameValueObject.allPlayerIds().contains(playerId)) {
				data.put("queryScope", QueryScope.gameFinishVote);
				// 通知其他人来查询
				List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService
						.findGamePlayerDbosForGame(gameId);
				gamePlayerDboList.forEach((gamePlayerDbo) -> {
					String otherPlayerId = gamePlayerDbo.getPlayerId();
					if (!otherPlayerId.equals(playerId)) {
						wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameFinishVote.name());
					}
				});
			} else {
				data.put("queryScope", null);
				// 通知其他人来查询
				List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService
						.findGamePlayerDbosForGame(gameId);
				gamePlayerDboList.forEach((gamePlayerDbo) -> {
					String otherPlayerId = gamePlayerDbo.getPlayerId();
					if (!otherPlayerId.equals(playerId)) {
						wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
					}
				});
			}
		}

		return vo;
	}

	@RequestMapping(value = "/vote_to_finish")
	@ResponseBody
	public CommonVO votetofinish(String token, boolean yes) {
		CommonVO vo = new CommonVO();
		Map data = new HashMap();
		vo.setData(data);
		String playerId = playerAuthService.getPlayerIdByToken(token);
		if (playerId == null) {
			vo.setSuccess(false);
			vo.setMsg("invalid token");
			return vo;
		}

		FinishResult finishResult;
		try {
			finishResult = gameCmdService.voteToFinish(playerId, yes);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		String gameId = finishResult.getGameValueObject().getId();
		majiangGameQueryService.voteToFinish(finishResult);
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			Map<String, MajiangGamePlayerDbo> playerMap = majiangPlayQueryService.findGamePlayersAsMap(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, playerMap, majiangGameDbo.getPanshu());
			ruianMajiangResultMsgService.recordJuResult(juResult);
			gameMsgService.gameFinished(gameId);
		}

		data.put("queryScope", QueryScope.gameFinishVote);
		// 通知其他人来查询投票情况
		List<MajiangGamePlayerDbo> gamePlayerDboList = majiangGameQueryService.findGamePlayerDbosForGame(gameId);
		gamePlayerDboList.forEach((gamePlayerDbo) -> {
			String otherPlayerId = gamePlayerDbo.getPlayerId();
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameFinishVote.name());
			}
		});
		return vo;

	}

	@RequestMapping(value = "/finish_vote_info")
	@ResponseBody
	public CommonVO finishvoteinfo(String gameId) {

		CommonVO vo = new CommonVO();
		GameFinishVoteDbo gameFinishVoteDbo = majiangGameQueryService.findGameFinishVoteDbo(gameId);
		Map data = new HashMap();
		data.put("vote", gameFinishVoteDbo.getVote());
		vo.setData(data);
		return vo;

	}

}
