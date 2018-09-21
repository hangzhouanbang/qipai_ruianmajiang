package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.finish.FixedPanNumbersJuFinishiDeterminer;
import com.dml.majiang.ju.firstpan.ClassicStartFirstPanProcess;
import com.dml.majiang.ju.nextpan.ClassicStartNextPanProcess;
import com.dml.majiang.pan.avaliablepai.NoHuapaiRandomAvaliablePaiFiller;
import com.dml.majiang.pan.frame.PanActionFrame;
import com.dml.majiang.pan.guipai.RandomGuipaiDeterminer;
import com.dml.majiang.pan.publicwaitingplayer.WaitDaPlayerPanPublicWaitingPlayerDeterminer;
import com.dml.majiang.player.action.chi.PengganghuFirstChiActionProcessor;
import com.dml.majiang.player.action.da.DachushoupaiDaActionProcessor;
import com.dml.majiang.player.action.gang.HuFirstGangActionProcessor;
import com.dml.majiang.player.action.guo.DoNothingGuoActionProcessor;
import com.dml.majiang.player.action.hu.PlayerHuAndClearAllActionHuActionUpdater;
import com.dml.majiang.player.action.hu.PlayerSetHuHuActionProcessor;
import com.dml.majiang.player.action.initial.ZhuangMoPaiInitialActionUpdater;
import com.dml.majiang.player.action.listener.comprehensive.DianpaoDihuOpportunityDetector;
import com.dml.majiang.player.action.listener.comprehensive.JuezhangStatisticsListener;
import com.dml.majiang.player.action.listener.gang.GangCounter;
import com.dml.majiang.player.action.listener.mo.MoGuipaiCounter;
import com.dml.majiang.player.action.peng.HuFirstPengActionProcessor;
import com.dml.majiang.player.menfeng.RandomMustHasDongPlayersMenFengDeterminer;
import com.dml.majiang.player.menfeng.ZhuangXiajiaIsDongIfZhuangNotHuPlayersMenFengDeterminer;
import com.dml.majiang.player.shoupai.gouxing.NoDanpaiOneDuiziGouXingPanHu;
import com.dml.majiang.player.zhuang.MenFengDongZhuangDeterminer;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.extend.fpmpv.FixedPlayersMultipanAndVotetofinishGame;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.player.GamePlayer;
import com.dml.mpgame.game.player.PlayerPlaying;

public class MajiangGame extends FixedPlayersMultipanAndVotetofinishGame {
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private Ju ju;
	private Map<String, Integer> playeTotalScoreMap = new HashMap<>();

	public PanActionFrame createJuAndStartFirstPan(long currentTime) throws Exception {
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
		ju.setCurrentPanFinishiDeterminer(new RuianMajiangPanFinishiDeterminer());
		ju.setGouXingPanHu(new NoDanpaiOneDuiziGouXingPanHu());
		ju.setCurrentPanPublicWaitingPlayerDeterminer(new WaitDaPlayerPanPublicWaitingPlayerDeterminer());
		RuianMajiangPanResultBuilder ruianMajiangPanResultBuilder = new RuianMajiangPanResultBuilder();
		ruianMajiangPanResultBuilder.setDapao(dapao);
		ruianMajiangPanResultBuilder.setDihu(difen);
		ju.setCurrentPanResultBuilder(ruianMajiangPanResultBuilder);
		ju.setJuFinishiDeterminer(new FixedPanNumbersJuFinishiDeterminer(panshu));
		ju.setJuResultBuilder(new RuianMajiangJuResultBuilder());
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
		ju.setHuActionProcessor(new PlayerSetHuHuActionProcessor());
		ju.setHuActionUpdater(new PlayerHuAndClearAllActionHuActionUpdater());

		ju.addActionStatisticsListener(new CaizipaiListener());
		ju.addActionStatisticsListener(new JuezhangStatisticsListener());
		ju.addActionStatisticsListener(new MoGuipaiCounter());
		ju.addActionStatisticsListener(new GangCounter());
		ju.addActionStatisticsListener(new DianpaoDihuOpportunityDetector());

		// 开始第一盘
		ju.startFirstPan(allPlayerIds());

		// 必然庄家已经先摸了一张牌了
		return ju.getCurrentPan().findLatestActionFrame();
	}

	public MajiangActionResult action(String playerId, int actionId, long actionTime) throws Exception {
		PanActionFrame panActionFrame = ju.action(playerId, actionId, actionTime);
		MajiangActionResult result = new MajiangActionResult();
		result.setPanActionFrame(panActionFrame);

		checkAndFinishPan();

		if (state.name().equals(WaitingNextPan.name) || state.name().equals(Finished.name)) {// 盘结束了
			RuianMajiangPanResult panResult = (RuianMajiangPanResult) ju.findLatestFinishedPanResult();
			for (RuianMajiangPanPlayerResult ruianMajiangPanPlayerResult : panResult.getPanPlayerResultList()) {
				playeTotalScoreMap.put(ruianMajiangPanPlayerResult.getPlayerId(),
						ruianMajiangPanPlayerResult.getTotalScore());
			}
			result.setPanResult(panResult);
			if (state.name().equals(Finished.name)) {// 局结束了
				result.setJuResult((RuianMajiangJuResult) ju.getJuResult());
			}
		}
		result.setMajiangGame(new MajiangGameValueObject(this));
		return result;
	}

	@Override
	protected boolean checkToFinishGame() throws Exception {
		return ju.getJuResult() != null;
	}

	@Override
	protected boolean checkToFinishCurrentPan() throws Exception {
		return ju.getCurrentPan() == null;
	}

	@Override
	protected void startNextPan() throws Exception {
		ju.startNextPan();
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	protected void updateToExtendedVotingState() {
	}

	@Override
	protected void updatePlayerToExtendedVotingState(GamePlayer player) {
	}

	@Override
	protected void recoveryStateFromExtendedVoting() throws Exception {
	}

	@Override
	protected void recoveryPlayersStateFromExtendedVoting() throws Exception {
	}

	@Override
	public void start() throws Exception {
		state = new Playing();
		updateAllPlayersState(new PlayerPlaying());
	}

	@Override
	protected void updatePlayerToExtendedVotedState(GamePlayer player) {
	}

	@Override
	public void finish() throws Exception {
		ju.finish();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected MajiangGameValueObject toValueObject() {
		return new MajiangGameValueObject(this);
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

	public Map<String, Integer> getPlayeTotalScoreMap() {
		return playeTotalScoreMap;
	}

	public void setPlayeTotalScoreMap(Map<String, Integer> playeTotalScoreMap) {
		this.playeTotalScoreMap = playeTotalScoreMap;
	}

}
