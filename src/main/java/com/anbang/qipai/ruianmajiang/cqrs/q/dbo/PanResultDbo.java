package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanResult;
import com.dml.majiang.pan.frame.PanActionFrame;

public class PanResultDbo {
	private String id;
	private String gameId;
	private int panNo;
	private String zhuangPlayerId;
	private boolean hu;
	private boolean zimo;
	private String dianpaoPlayerId;
	private List<RuianMajiangPanPlayerResultDbo> playerResultList;
	private long finishTime;
	private PanActionFrame panActionFrame;

	public PanResultDbo() {
	}

	public PanResultDbo(String gameId, RuianMajiangPanResult ruianMajiangPanResult) {
		this.gameId = gameId;
		panNo = ruianMajiangPanResult.getPan().getNo();
		zhuangPlayerId = ruianMajiangPanResult.findZhuangPlayerId();
		hu = ruianMajiangPanResult.isHu();
		zimo = ruianMajiangPanResult.isZimo();
		dianpaoPlayerId = ruianMajiangPanResult.getDianpaoPlayerId();
		playerResultList = new ArrayList<>();
		for (RuianMajiangPanPlayerResult playerResult : ruianMajiangPanResult.getPanPlayerResultList()) {
			RuianMajiangPanPlayerResultDbo dbo = new RuianMajiangPanPlayerResultDbo();
			dbo.setPlayerId(playerResult.getPlayerId());
			dbo.setPlayerResult(playerResult);
			dbo.setPlayer(ruianMajiangPanResult.findPlayer(playerResult.getPlayerId()));
			playerResultList.add(dbo);
		}

		finishTime = ruianMajiangPanResult.getPanFinishTime();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
	}

	public boolean isHu() {
		return hu;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public boolean isZimo() {
		return zimo;
	}

	public void setZimo(boolean zimo) {
		this.zimo = zimo;
	}

	public String getDianpaoPlayerId() {
		return dianpaoPlayerId;
	}

	public void setDianpaoPlayerId(String dianpaoPlayerId) {
		this.dianpaoPlayerId = dianpaoPlayerId;
	}

	public List<RuianMajiangPanPlayerResultDbo> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResultDbo> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public PanActionFrame getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrame panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

}
