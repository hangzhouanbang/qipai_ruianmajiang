package com.anbang.qipai.ruianmajiang.cqrs.q.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.q.dao.GamePlayerDboDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dao.MajiangGameDao;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.anbang.qipai.ruianmajiang.plan.dao.PlayerInfoDao;
import com.dml.mpgame.GamePlayerState;

@Component
public class MajiangGameQueryService {

	@Autowired
	private MajiangGameDao majiangGameDao;

	@Autowired
	private PlayerInfoDao playerInfoDao;

	@Autowired
	private GamePlayerDboDao gamePlayerDboDao;

	public MajiangGameDbo findMajiangGameDboById(String gameId) {
		return majiangGameDao.findById(gameId);
	}

	public void newMajiangGame(String newGameId, String playerId, int difen, int taishu, int panshu, int renshu,
			boolean dapao) {

		MajiangGameDbo majiangGameDbo = new MajiangGameDbo();
		majiangGameDbo.setDapao(dapao);
		majiangGameDbo.setDifen(difen);
		majiangGameDbo.setId(newGameId);
		majiangGameDbo.setPanshu(panshu);
		majiangGameDbo.setTaishu(taishu);
		majiangGameDbo.setRenshu(renshu);
		majiangGameDao.save(majiangGameDbo);

		PlayerInfo playerInfo = playerInfoDao.findById(playerId);
		String nickname = null;
		String headimgurl = null;
		if (playerInfo != null) {
			nickname = playerInfo.getNickname();
			headimgurl = playerInfo.getHeadimgurl();
		}

		GamePlayerDbo gamePlayerDbo = new GamePlayerDbo();
		gamePlayerDbo.setGameId(newGameId);
		gamePlayerDbo.setHeadimgurl(headimgurl);
		gamePlayerDbo.setNickname(nickname);
		gamePlayerDbo.setPlayerId(playerId);
		gamePlayerDbo.setState(GamePlayerState.joined.name());
		gamePlayerDboDao.save(gamePlayerDbo);

	}

	public List<GamePlayerDbo> findGamePlayerDbosForGame(String gameId) {
		return gamePlayerDboDao.findByGameId(gameId);
	}

}
