package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.avaliablepai.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.pan.finish.PlayerHuOrNoPaiLeftPanFinishiDeterminer;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.RandomGuipaiDeterminer;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.player.action.chi.PengganghuFirstChiActionProcessor;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.HuFirstGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.mo.MoGuipaiCounter;
import com.dml.majiang.player.action.peng.HuFirstPengActionProcessor;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.NoDanpaiOneDuiziGouXingPanHu;
import com.dml.majiang.player.zhuang.MenFengDongZhuangDeterminer;
import com.dml.mpgame.Game;
import com.dml.mpgame.GameState;
import com.dml.mpgame.GameValueObject;

public class MajiangGame {
	private Game game;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Ju ju;

	public JoinGameResult join(String playerId) throws Exception {
		JoinGameResult result = new JoinGameResult();
		game.join(playerId);
		result.setGameId(game.getId());
		List<String> playerIds = game.allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);
		return result;
	}

	public GameValueObject leave(String playerId) throws Exception {
		return game.leave(playerId);
	}

	public void back(String playerId) throws Exception {
		game.back(playerId);
	}

	public ReadyForGameResult ready(String playerId, long currentTime) throws Exception {
		game.ready(playerId);

		MajiangActionResult firstActionResult = null;
		if (game.getState().equals(GameState.playing)) {// 游戏开始了，那么要创建新的局
			ju = new Ju();
			ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
			ju.setZhuangDeterminerForFirstPan(new MenFengDongZhuangDeterminer());
			ju.setAvaliablePaiFiller(new NoHuapaiRandomAvaliablePaiFiller(currentTime + 1));
			ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 2));
			ju.setFaPaiStrategy(new RuianMajiangFaPaiStrategy(16));
			ju.setCurrentPanFinishiDeterminer(new PlayerHuOrNoPaiLeftPanFinishiDeterminer());
			ju.setGouXingPanHu(new NoDanpaiOneDuiziGouXingPanHu());
			ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());
			ju.setCurrentPanResultBuilder(new RuianMajiangPanResultBuilder());
			ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
			RuianMajiangJuResultBuilder ruianMajiangJuResultBuilder = new RuianMajiangJuResultBuilder();
			ruianMajiangJuResultBuilder.setDihu(difen);
			ju.setJuResultBuilder(ruianMajiangJuResultBuilder);
			ju.setInitialActionUpdater(new ZhuangMoPaiInitialActionUpdater());
			ju.setMoActionProcessor(new RuianMajiangMoActionProcessor());
			ju.setMoActionUpdater(new RuianMajiangMoActionUpdater());
			ju.setDaActionProcessor(new DachushoupaiDaActionProcessor());
			ju.setDaActionUpdater(new RuianMajiangDaActionUpdater());
			ju.setChiActionProcessor(new PengganghuFirstChiActionProcessor());
			ju.setChiActionUpdater(new RuianMajiangChiActionUpdater());
			ju.setPengActionProcessor(new HuFirstPengActionProcessor());
			ju.setPengActionUpdater(new RuianMajiangPengActionUpdater());
			ju.setGangActionProcessor(new HuFirstGangActionProcessor());
			ju.setGangActionUpdater(new RuianMajiangGangActionUpdater());
			ju.setGuoActionProcessor(new DoNothingGuoActionProcessor());
			ju.setGuoActionUpdater(new RuianMajiangGuoActionUpdater());
			ju.setHuActionProcessor(new RuianMajiangHuActionProcessor());

			ju.addActionStatisticsListener(new CaizipaiListener());
			ju.addActionStatisticsListener(new MoGuipaiCounter());

			Pan firstPan = new Pan();
			firstPan.setNo(1);
			game.allPlayerIds().forEach((pid) -> firstPan.addPlayer(pid));
			ju.setCurrentPan(firstPan);

			// 开始定第一盘的门风
			ju.determinePlayersMenFengForFirstPan();

			// 开始定第一盘庄家
			ju.determineZhuangForFirstPan();

			// 开始填充可用的牌
			ju.fillAvaliablePai();

			// 开始定财神
			ju.determineGuipai();

			// 开始发牌
			ju.faPai();

			// 庄家可以摸第一张牌
			ju.updateInitialAction();

			// 庄家摸第一张牌,进入正式行牌流程
			firstActionResult = action(ju.getCurrentPan().getZhuangPlayerId(), 1, currentTime);
		}

		ReadyForGameResult result = new ReadyForGameResult();
		result.setGame(new GameValueObject(game));
		if (firstActionResult != null) {
			result.setFirstActionFrame(firstActionResult.getPanActionFrame());
		}
		List<String> playerIds = game.allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);

		return result;

	}

	public MajiangActionResult action(String playerId, int actionId, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.action(playerId, actionId, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);
		if (ju.getCurrentPan() == null) {// 盘结束了
			result.setPanResult((RuianMajiangPanResult) ju.findLatestFinishedPanResult());
		}
		// TODO 局是否结束?局结果
		result.setGame(new GameValueObject(game));
		List<String> playerIds = game.allPlayerIds();
		playerIds.remove(playerId);
		result.setOtherPlayerIds(playerIds);
		return result;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getDifen() {
		return difen;
	}

	public void setDifen(int difen) {
		this.difen = difen;
	}

	public int getTaishu() {
		return taishu;
	}

	public void setTaishu(int taishu) {
		this.taishu = taishu;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public Ju getJu() {
		return ju;
	}

	public void setJu(Ju ju) {
		this.ju = ju;
	}

}
