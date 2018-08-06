package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.ju.firstpan.ClassicStartFirstPanProcess;
import com.dml.majiang.ju.nextpan.AllPlayersReadyCreateNextPanDeterminer;
import com.dml.majiang.ju.nextpan.ClassicStartNextPanProcess;
import com.dml.majiang.pan.avaliablepai.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.pan.finish.PlayerHuOrNoPaiLeftPanFinishiDeterminer;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.RandomGuipaiDeterminer;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.player.action.chi.ChiPlayerDaPaiChiActionUpdater;
import com.dml.majiang.player.action.chi.PengganghuFirstChiActionProcessor;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.GangPlayerMoPaiGangActionUpdater;
import com.dml.majiang.player.action.gang.HuFirstGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.guo.PlayerDaPaiOrXiajiaMoPaiGuoActionUpdater;
import com.dml.majiang.player.action.hu.PlayerSetHuHuActionProcessor;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.mo.MoGuipaiCounter;
import com.dml.majiang.player.action.peng.HuFirstPengActionProcessor;
import com.dml.majiang.player.action.peng.RuianMajiangPengActionUpdater;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.menfeng.ZhuangXiajiaIsDongIfZhuangNotHuPlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.NoDanpaiOneDuiziGouXingPanHu;
import com.dml.majiang.player.zhuang.MenFengDongZhuangDeterminer;
import com.dml.mpgame.GameValueObject;

public class MajiangGame {
	private String gameId;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Ju ju;

	public PanActionFrame createJuAndStartFirstPan(GameValueObject game, long currentTime) throws Exception {
		ju = new Ju();
		ju.setStartFirstPanProcess(new ClassicStartFirstPanProcess());
		ju.setStartNextPanProcess(new ClassicStartNextPanProcess());
		ju.setPlayersMenFengDeterminerForFirstPan(new RandomMustHasDongPlayersMenFengDeterminer(currentTime));
		ju.setPlayersMenFengDeterminerForNextPan(new ZhuangXiajiaIsDongIfZhuangNotHuPlayersMenFengDeterminer());
		ju.setZhuangDeterminerForFirstPan(new MenFengDongZhuangDeterminer());
		ju.setZhuangDeterminerForNextPan(new MenFengDongZhuangDeterminer());
		ju.setAvaliablePaiFiller(new NoHuapaiRandomAvaliablePaiFiller(currentTime + 1));
		ju.setGuipaiDeterminer(new RandomGuipaiDeterminer(currentTime + 2));
		ju.setFaPaiStrategy(new RuianMajiangFaPaiStrategy(16));
		ju.setCurrentPanFinishiDeterminer(new PlayerHuOrNoPaiLeftPanFinishiDeterminer());
		ju.setGouXingPanHu(new NoDanpaiOneDuiziGouXingPanHu());
		ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());
		ju.setCurrentPanResultBuilder(new RuianMajiangPanResultBuilder());
		AllPlayersReadyCreateNextPanDeterminer createNextPanDeterminer = new AllPlayersReadyCreateNextPanDeterminer();
		game.allPlayerIds().forEach((pid) -> createNextPanDeterminer.addPlayer(pid));
		ju.setCreateNextPanDeterminer(createNextPanDeterminer);
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
		ju.setChiActionUpdater(new ChiPlayerDaPaiChiActionUpdater());
		ju.setPengActionProcessor(new HuFirstPengActionProcessor());
		ju.setPengActionUpdater(new RuianMajiangPengActionUpdater());
		ju.setGangActionProcessor(new HuFirstGangActionProcessor());
		ju.setGangActionUpdater(new GangPlayerMoPaiGangActionUpdater());
		ju.setGuoActionProcessor(new DoNothingGuoActionProcessor());
		ju.setGuoActionUpdater(new PlayerDaPaiOrXiajiaMoPaiGuoActionUpdater());
		ju.setHuActionProcessor(new PlayerSetHuHuActionProcessor());

		ju.addActionStatisticsListener(new CaizipaiListener());
		ju.addActionStatisticsListener(new MoGuipaiCounter());

		// 开始第一盘
		ju.startFirstPan(game.allPlayerIds());

		// 必然庄家已经先摸了一张牌了
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public MajiangActionResult action(String playerId, int actionId, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.action(playerId, actionId, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);
		if (ju.getCurrentPan() == null) {// 盘结束了
			result.setPanResult((RuianMajiangPanResult) ju.findLatestFinishedPanResult());
		}
		if (ju.getJuResult() != null) {// 局结束了
			result.setJuResult((RuianMajiangJuResult) ju.getJuResult());
		}
		return result;
	}

	public PanActionFrame readyToNextPan(String playerId) throws Exception {
		ReadyToNextPanResult readyToNextPanResult = new ReadyToNextPanResult();
		AllPlayersReadyCreateNextPanDeterminer createNextPanDeterminer = (AllPlayersReadyCreateNextPanDeterminer) ju
				.getCreateNextPanDeterminer();
		createNextPanDeterminer.playerReady(playerId);
		// 如果可以创建下一盘,那就创建下一盘
		if (ju.determineToCreateNextPan()) {
			ju.startNextPan();
			// 必然庄家已经先摸了一张牌了
			return ju.getCurrentPan().findLatestActionFrame();
		} else {
			return null;
		}
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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
