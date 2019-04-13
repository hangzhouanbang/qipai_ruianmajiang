package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyForGameResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.JuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanActionFrameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.PanResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedGameLatestPanActionFrameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedJuResultDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedMajiangGameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedPanActionFrameDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.memcached.MemcachedPanResultDboDao;
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
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private MajiangGameDboDao majiangGameDboDao;

	@Autowired
	private PanResultDboDao panResultDboDao;

	@Autowired
	private JuResultDboDao juResultDboDao;

	@Autowired
	private PanActionFrameDboDao panActionFrameDboDao;

	@Autowired
	private MemcachedMajiangGameDboDao memcachedMajiangGameDboDao;

	@Autowired
	private MemcachedPanResultDboDao memcachedPanResultDboDao;

	@Autowired
	private MemcachedJuResultDboDao memcachedJuResultDboDao;

	@Autowired
	private MemcachedGameLatestPanActionFrameDboDao memcachedGameLatestPanActionFrameDboDao;

	@Autowired
	private MemcachedPanActionFrameDboDao memcachedPanActionFrameDboDao;

	private ExecutorService executorService = Executors.newCachedThreadPool();

	private LiangangangPanActionFramePlayerViewFilter pvFilter = new LiangangangPanActionFramePlayerViewFilter();

	public PanActionFrame findAndFilterCurrentPanValueObjectForPlayer(String gameId, String playerId) throws Exception {
		MajiangGameDbo majiangGameDbo = memcachedMajiangGameDboDao.findById(gameId);
		if (!(majiangGameDbo.getState().equals(Playing.name) || majiangGameDbo.getState().equals(VotingWhenPlaying.name)
				|| majiangGameDbo.getState().equals(VoteNotPassWhenPlaying.name))) {
			throw new Exception("game not playing");
		}

		GameLatestPanActionFrameDbo frame = memcachedGameLatestPanActionFrameDboDao.findById(gameId);
		byte[] frameData = frame.getData();
		PanActionFrame panActionFrame = PanActionFrame.fromByteArray(frameData);
		pvFilter.filter(panActionFrame, playerId);
		return panActionFrame;
	}

	public int findCurrentPanLastestActionNo(String gameId) throws Exception {
		GameLatestPanActionFrameDbo frame = memcachedGameLatestPanActionFrameDboDao.findById(gameId);
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
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		if (majiangGame.getState().name().equals(Playing.name)) {
			PanActionFrame panActionFrame = readyForGameResult.getFirstActionFrame();
			memcachedGameLatestPanActionFrameDboDao.save(majiangGame.getId(), panActionFrame.toByteArray(1024 * 8));
			// 记录一条Frame，回放的时候要做
			String gameId = majiangGame.getId();
			int panNo = panActionFrame.getPanAfterAction().getNo();
			int actionNo = panActionFrame.getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(panActionFrame);
			memcachedPanActionFrameDboDao.save(panActionFrameDbo);
		}
	}

	public void xipai(MajiangGameValueObject majiangGame) throws Throwable {
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);
	}

	public void readyToNextPan(ReadyToNextPanResult readyToNextPanResult) throws Throwable {
		MajiangGameValueObject majiangGame = readyToNextPanResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((pid) -> playerInfoMap.put(pid, playerInfoDao.findById(pid)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		if (readyToNextPanResult.getFirstActionFrame() != null) {
			memcachedGameLatestPanActionFrameDboDao.save(majiangGame.getId(),
					readyToNextPanResult.getFirstActionFrame().toByteArray(1024 * 8));
			// 记录一条Frame，回放的时候要做
			String gameId = majiangGame.getId();
			int panNo = readyToNextPanResult.getFirstActionFrame().getPanAfterAction().getNo();
			int actionNo = readyToNextPanResult.getFirstActionFrame().getNo();
			PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
			panActionFrameDbo.setPanActionFrame(readyToNextPanResult.getFirstActionFrame());
			memcachedPanActionFrameDboDao.save(panActionFrameDbo);
		}
	}

	public void action(MajiangActionResult majiangActionResult) throws Throwable {
		MajiangGameValueObject majiangGame = majiangActionResult.getMajiangGame();
		Map<String, PlayerInfo> playerInfoMap = new HashMap<>();
		majiangGame.allPlayerIds().forEach((playerId) -> playerInfoMap.put(playerId, playerInfoDao.findById(playerId)));
		MajiangGameDbo majiangGameDbo = new MajiangGameDbo(majiangGame, playerInfoMap);
		memcachedMajiangGameDboDao.save(majiangGameDbo);

		String gameId = majiangGame.getId();
		PanActionFrame panActionFrame = majiangActionResult.getPanActionFrame();
		memcachedGameLatestPanActionFrameDboDao.save(gameId, panActionFrame.toByteArray(1024 * 8));
		// 记录一条Frame，回放的时候要做
		int panNo = panActionFrame.getPanAfterAction().getNo();
		int actionNo = panActionFrame.getNo();
		PanActionFrameDbo panActionFrameDbo = new PanActionFrameDbo(gameId, panNo, actionNo);
		panActionFrameDbo.setPanActionFrame(panActionFrame);
		memcachedPanActionFrameDboDao.save(panActionFrameDbo);
		// 盘出结果的话要记录结果
		RuianMajiangPanResult ruianMajiangPanResult = majiangActionResult.getPanResult();
		if (ruianMajiangPanResult != null) {
			PanResultDbo panResultDbo = new PanResultDbo(gameId, ruianMajiangPanResult);
			panResultDbo.setPanActionFrame(panActionFrame);
			executorService.submit(() -> {
				panResultDboDao.save(panResultDbo);
				try {
					List<PanActionFrameDbo> frameList = memcachedPanActionFrameDboDao.findByGameIdAndActionNo(gameId,
							panNo, actionNo);
					panActionFrameDboDao.save(frameList);
					// memcached不提供批量删除，如果循环删除则代价太高，这里通过key来覆盖，减少缓存
					// memcachedPanActionFrameDboDao.removePanActionFrame(gameId, panNo, actionNo);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
			if (majiangActionResult.getJuResult() != null) {// 一切都结束了
				// 要记录局结果
				JuResultDbo juResultDbo = new JuResultDbo(gameId, panResultDbo, majiangActionResult.getJuResult());
				executorService.submit(() -> {
					majiangGameDboDao.save(majiangGameDbo);
					juResultDboDao.save(juResultDbo);
				});
				memcachedJuResultDboDao.save(juResultDbo);
			}
			memcachedPanResultDboDao.save(panResultDbo);
		}
	}

	public PanResultDbo findPanResultDbo(String gameId, int panNo) throws Exception {
		return memcachedPanResultDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

	public JuResultDbo findJuResultDbo(String gameId) throws Exception {
		return memcachedJuResultDboDao.findByGameId(gameId);
	}

	public List<PanActionFrameDbo> findPanActionFrameDboForBackPlay(String gameId, int panNo) {
		return panActionFrameDboDao.findByGameIdAndPanNo(gameId, panNo);
	}

}
