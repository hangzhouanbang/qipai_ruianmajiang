package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGameDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.RuianMajiangPanPlayerResultDbo;

public class PanResultVO {

	private List<RuianMajiangPanPlayerResultVO> playerResultList;

	private boolean hu;

	private int panNo;

	private long finishTime;

	private int paiCount;

	private PanActionFrameVO panActionFrame;

	public PanResultVO(PanResultDbo dbo, MajiangGameDbo majiangGameDbo) {
		List<RuianMajiangPanPlayerResultDbo> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList
					.add(new RuianMajiangPanPlayerResultVO(majiangGameDbo.findPlayer(panPlayerResult.getPlayerId()),
							dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		hu = dbo.isHu();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
		paiCount = dbo.getPanActionFrame().getPanAfterAction().getAvaliablePaiList().getPaiCount();
		panActionFrame = new PanActionFrameVO(dbo.getPanActionFrame());
	}

	public PanActionFrameVO getPanActionFrame() {
		return panActionFrame;
	}

	public void setPanActionFrame(PanActionFrameVO panActionFrame) {
		this.panActionFrame = panActionFrame;
	}

	public void setPlayerResultList(List<RuianMajiangPanPlayerResultVO> playerResultList) {
		this.playerResultList = playerResultList;
	}

	public void setHu(boolean hu) {
		this.hu = hu;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}

	public List<RuianMajiangPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public boolean isHu() {
		return hu;
	}

	public long getFinishTime() {
		return finishTime;
	}

	public int getPanNo() {
		return panNo;
	}

	public int getPaiCount() {
		return paiCount;
	}

	public void setPaiCount(int paiCount) {
		this.paiCount = paiCount;
	}

}
