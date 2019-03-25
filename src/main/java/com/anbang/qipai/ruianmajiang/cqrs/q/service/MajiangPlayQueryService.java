package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GameLatestPanActionFrameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanActionFrameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GameLatestPanActionFrameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanActionFrameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.dml.majiang.pan.frame.LiangangangPanActionFramePlayerViewFilter;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;

@Component
public class MajiangPlayQueryService {

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GameLatestPanActionFrameDboDao gameLatestPanActionFrameDboDao;

	@Autowired
	private PanActionFrameDboDao panActionFrameDboDao;

	private LiangangangPanActionFramePlayerViewFilter pvFilter = new LiangangangPanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		MajiangGameDbo majiangGameDbo = majiangGameDboDao.findById(gameId);
		if (!(majiangGameDbo.getState().name().equals(Playing.name)
				|| majiangGameDbo.getState().name().equals(VotingWhenPlaying.name)
				|| majiangGameDbo.getState().name().equals(VoteNotPassWhenPlaying.name))) {
			throw new Exception("game not playing");
		}

		GameLatestPanActionFrameDbo frame = gameLatestPanActionFrameDboDao.findById(gameId);
		byte[] frameData = frame.getData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public int findCurrentPanLastestActionNo(String gameId) {
		GameLatestPanActionFrameDbo frame = gameLatestPanActionFrameDboDao.findById(gameId);
		if (frame == null) {
			return 0;
		}
		byte[] frameData = frame.getData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		return panActionFrame.getNo();
	}

	public void readyForGame(ReadyForGameResult readyForGameResult) throws Throwable {
		MajiangGameValueObject majiangGame = readyForGameResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (majiangGame.getState().name().equals(Playing.name)) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			gameLatestPanActionFrameDboDao.save(majiangGame.getId(), panActionFrame.toByteArray(1024 * 8));
			// 记录一条Frame，回放的时候要做
			String gameId = majiangGame.getId();
			int panNo = panActionFrame.getPanAfterAction().getNo();
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			panActionFrameDboDao.save(panActionFrameDbo);
		}
	}

	public void xipai(MajiangGameValueObject majiangGame) {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);
	}

	public void readyToNextPan(ReadyToNextPanResult readyToNextPanResult) throws Throwable {

		MajiangGameValueObject majiangGame = readyToNextPanResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			gameLatestPanActionFrameDboDao.save(majiangGame.getId(),
					readyToNextPanResult.getFirstActionFrame().toByteArray(1024 * 8));
			// 记录一条Frame，回放的时候要做
			String gameId = majiangGame.getId();
			int panNo = readyToNextPanResult.getFirstActionFrame().getPanAfterAction().getNo();
			int actionNo = readyToNextPanResult.getFirstActionFrame().getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(readyToNextPanResult.getFirstActionFrame());
			panActionFrameDboDao.save(panActionFrameDbo);
		}

	}

	public void action(MajiangActionResult majiangActionResult) throws Throwable {

		MajiangGameValueObject majiangGame = majiangActionResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		majiangGameDboDao.save(majiangGameDbo);

		String gameId = majiangGame.getId();
		PanActionFrame panActionFrame = majiangActionResult.getPanActionFrame();
		gameLatestPanActionFrameDboDao.save(gameId, panActionFrame.toByteArray(1024 * 8));
		// 记录一条Frame，回放的时候要做
		int panNo = panActionFrame.getPanAfterAction().getNo();
		int actionNo = panActionFrame.getNo();
		PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
		panActionFrameDbo.setPanActionFrame(panActionFrame);
		panActionFrameDboDao.save(panActionFrameDbo);
		// 盘出结果的话要记录结果
		RuianMajiangPanResult ruianMajiangPanResult = majiangActionResult.getPanResult();
		if (ruianMajiangPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, ruianMajiangPanResult);
			panResultDbo.setPanActionFrame(panActionFrame);
			panResultDboDao.save(panResultDbo);
			if (majiangActionResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, majiangActionResult.getJuResult());
				juResultDboDao.save(juResultDbo);
			}
		}
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) {
		return panResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public JuResultDbo findJuResultDbo(String gameId) {
		return juResultDboDao.findByGameId(gameId);
	}

	public List<PanActionFrameDbo> findPanActionFrameDboForBackPlay(String gameId, int panNo) {
		return panActionFrameDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

}
