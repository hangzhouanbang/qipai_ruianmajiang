package com.anbang.qipai.ruianmajiang.msg.msjobj;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;

public class MajiangHistoricalJuResult {
	private String gameId;
	private String dayingjiaId;
	private String datuhaoId;
	private List<RuianMajiangJuPlayerResultMO> playerResultList;
	private int lastPanNo;
	private int panshu;
	private long finishTime;

	public MajiangHistoricalJuResult() {
	}

	public MajiangHistoricalJuResult(JuResultDbo juResultDbo, MajiangGameDbo majiangGameDbo) {
		gameId = juResultDbo.getGameId();
		RuianMajiangJuResult ruianMajiangJuResult = juResultDbo.getJuResult();
		dayingjiaId = ruianMajiangJuResult.getDayingjiaId();
		datuhaoId = ruianMajiangJuResult.getDatuhaoId();
		finishTime = juResultDbo.getFinishTime();
		this.panshu = majiangGameDbo.getPanshu();
		lastPanNo = ruianMajiangJuResult.getFinishedPanCount();
		playerResultList = new ArrayList<>();
		if (ruianMajiangJuResult.getPlayerResultList() != null) {
			ruianMajiangJuResult.getPlayerResultList()
					.forEach((juPlayerResult) -> playerResultList.add(new RuianMajiangJuPlayerResultMO(juPlayerResult,
							majiangGameDbo.findPlayer(juPlayerResult.getPlayerId()))));
		} else {
			majiangGameDbo.getPlayers().forEach((majiangGamePlayerDbo) -> playerResultList
					.add(new RuianMajiangJuPlayerResultMO(majiangGamePlayerDbo)));
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

	public List<RuianMajiangJuPlayerResultMO> getPlayerResultList() {
		return playerResultList;
	}

	public void setPlayerResultList(List<RuianMajiangJuPlayerResultMO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public int getLastPanNo() {
		return lastPanNo;
	}

	public void setLastPanNo(int lastPanNo) {
		this.lastPanNo = lastPanNo;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
