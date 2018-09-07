package com.anbang.qipai.ruianmajiang.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.dml.mpgame.game.finish.vote.VoteAfterStartedGameFinishStrategy;
import com.dml.mpgame.game.finish.vote.VoteAfterStartedGameFinishStrategyValueObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.FinishResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameState;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.GameCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.PlayerAuthService;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameFinishVoteDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangGameQueryService;
import com.anbang.qipai.ruianmajiang.cqrs.q.service.MajiangPlayQueryService;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangGameMsgService;
import com.anbang.qipai.ruianmajiang.msg.service.RuianMajiangResultMsgService;
import com.anbang.qipai.ruianmajiang.web.vo.CommonVO;
import com.anbang.qipai.ruianmajiang.web.vo.GameVO;
import com.anbang.qipai.ruianmajiang.web.vo.JuResultVO;
import com.anbang.qipai.ruianmajiang.websocket.GamePlayWsNotifier;
import com.anbang.qipai.ruianmajiang.websocket.QueryScope;

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
		MajiangGameValueObject majiangGameValueObject = gameCmdService.newMajiangGame(newGameId, playerId, difen,
				taishu, panshu, renshu, dapao);
		majiangGameQueryService.newMajiangGame(majiangGameValueObject);
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
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.joinGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}
		majiangGameQueryService.joinGame(majiangGameValueObject);
		// 通知其他人
		for (String otherPlayerId : majiangGameValueObject.allPlayerIds()) {
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
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.leaveGame(playerId);
			if (majiangGameValueObject == null) {
				vo.setSuccess(true);
				return vo;
			}
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().getName());
			return vo;
		}
		majiangGameQueryService.leaveGame(majiangGameValueObject);
		gameMsgService.gamePlayerLeave(majiangGameValueObject, playerId);
		// 通知其他玩家

		majiangGameValueObject.allPlayerIds().forEach((otherPlayerId) -> {
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
		MajiangGameValueObject majiangGameValueObject;
		try {
			majiangGameValueObject = gameCmdService.backToGame(playerId, gameId);
		} catch (Exception e) {
			vo.setSuccess(false);
			vo.setMsg(e.getClass().toString());
			return vo;
		}

		majiangGameQueryService.backToGame(playerId, majiangGameValueObject);

		// 通知其他玩家
		majiangGameValueObject.allPlayerIds().forEach((otherPlayerId) -> {
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
		GameVO gameVO = new GameVO(majiangGameDbo);
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
		for (String otherPlayerId : readyForGameResult.getMajiangGame().allPlayerIds()) {
			if (!otherPlayerId.equals(playerId)) {
				wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
				if (readyForGameResult.getMajiangGame().getState().equals(MajiangGameState.playing)) {
					wsNotifier.notifyToQuery(otherPlayerId, QueryScope.panForMe.name());
				}
			}
		}

		List<QueryScope> queryScopes = new ArrayList<>();
		queryScopes.add(QueryScope.gameInfo);
		if (readyForGameResult.getMajiangGame().getState().equals(MajiangGameState.playing)) {
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
		MajiangGameValueObject majiangGameValueObject = finishResult.getMajiangGameValueObject();
		String gameId = majiangGameValueObject.getGameId();
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, majiangGameDbo);
			ruianMajiangResultMsgService.recordJuResult(juResult);
			gameMsgService.gameFinished(gameId);
		}

		if (majiangGameValueObject.getState().equals(MajiangGameState.finished)) {
			data.put("queryScope", QueryScope.gameInfo);
			// 通知其他人来查询
			majiangGameValueObject.allPlayerIds().forEach((otherPlayerId) -> {
				if (!otherPlayerId.equals(playerId)) {
					wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameInfo.name());
				}
			});
		} else {
			// 游戏没结束有两种可能：一种是发起了投票。还有一种是游戏没开始，解散发起人又不是房主，那就自己走人。
			final VoteAfterStartedGameFinishStrategyValueObject finishStrategy=finishResult.getVoteFinishStrategy();
			if (finishStrategy.getVote()!=null) {
				data.put("queryScope", QueryScope.gameFinishVote);
				// 通知其他人来查询
				majiangGameValueObject.allPlayerIds().forEach((otherPlayerId) -> {
					if (!otherPlayerId.equals(playerId)) {
						wsNotifier.notifyToQuery(otherPlayerId, QueryScope.gameFinishVote.name());
					}
				});
			} else {
				data.put("queryScope", null);
				gameMsgService.gameFinished(gameId);
				// 通知其他人来查询
				majiangGameValueObject.allPlayerIds().forEach((otherPlayerId) -> {
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
		String gameId = finishResult.getMajiangGameValueObject().getGameId();
		majiangGameQueryService.voteToFinish(finishResult);
		JuResultDbo juResultDbo = majiangPlayQueryService.findJuResultDbo(gameId);
		// 记录战绩
		if (juResultDbo != null) {
			MajiangGameDbo majiangGameDbo = majiangGameQueryService.findMajiangGameDboById(gameId);
			JuResultVO juResult = new JuResultVO(juResultDbo, majiangGameDbo);
			ruianMajiangResultMsgService.recordJuResult(juResult);
			gameMsgService.gameFinished(gameId);
		}

		data.put("queryScope", QueryScope.gameFinishVote);
		// 通知其他人来查询投票情况
		finishResult.getMajiangGameValueObject().allPlayerIds().forEach((otherPlayerId) -> {
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
