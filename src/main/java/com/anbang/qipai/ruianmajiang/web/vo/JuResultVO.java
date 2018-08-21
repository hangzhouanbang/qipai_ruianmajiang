package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class JuResultVO {

	private String gameId;
	private String dayingjiaId;
	private String datuhaoId;
	private int panshu;
	private int finishedPanCount;
	private List<RuianMajiangJuPlayerResultVO> playerResultList;

	private PanResultVO lastPanResult;
	private long finishTime;

	public JuResultVO(JuResultDbo juResultDbo, Map<String, MajiangGamePlayerDbo> playerMap, int panshu) {
		gameId = juResultDbo.getGameId();
		RuianMajiangJuResult ruianMajiangJuResult = juResultDbo.getJuResult();
		dayingjiaId = ruianMajiangJuResult.getDayingjiaId();
		datuhaoId = ruianMajiangJuResult.getDatuhaoId();
		if (juResultDbo.getLastPanResult() != null) {
			lastPanResult = new PanResultVO(juResultDbo.getLastPanResult(), playerMap);
			finishTime = lastPanResult.getFinishTime();
		} else {
			finishTime = System.currentTimeMillis();
		}
		this.panshu = panshu;
		finishedPanCount = ruianMajiangJuResult.getFinishedPanCount();
		playerResultList = new ArrayList<>();
		if (ruianMajiangJuResult.getPlayerResultList() != null) {
			ruianMajiangJuResult.getPlayerResultList().forEach((juPlayerResult) -> playerResultList.add(
					new RuianMajiangJuPlayerResultVO(juPlayerResult, playerMap.get(juPlayerResult.getPlayerId()))));
		} else {
			playerMap.values().forEach((majiangGamePlayerDbo) -> playerResultList
					.add(new RuianMajiangJuPlayerResultVO(majiangGamePlayerDbo)));
		}
	}

	public String getDayingjiaId() {
		return dayingjiaId;
	}

	public void setDayingjiaId(String dayingjiaId) {
		this.dayingjiaId = dayingjiaId;
	}

	public String getDatuhaoId() {
		return datuhaoId;
	}

	public void setDatuhaoId(String datuhaoId) {
		this.datuhaoId = datuhaoId;
	}

	public List<RuianMajiangJuPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangJuPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public PanResultVO getLastPanResult() {
		return lastPanResult;
	}

	public void setLastPanResult(PanResultVO lastPanResult) {
		this.lastPanResult = lastPanResult;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getFinishedPanCount() {
		return finishedPanCount;
	}

	public void setFinishedPanCount(int finishedPanCount) {
		this.finishedPanCount = finishedPanCount;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

}
