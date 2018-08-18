package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;

public class PanResultVO {

	private List<RuianMajiangPanPlayerResultVO> playerResultList;

	private boolean hu;

	private int panNo;

	private long finishTime;

	public PanResultVO(PanResultDbo dbo, Map<String, MajiangGamePlayerDbo> playerMap) {
		List<RuianMajiangPanPlayerResult> list = dbo.getPlayerResultList();
		if (list != null) {
			playerResultList = new ArrayList<>(list.size());
			list.forEach((panPlayerResult) -> playerResultList
					.add(new RuianMajiangPanPlayerResultVO(playerMap.get(panPlayerResult.getPlayerId()),
							dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		}
		hu = dbo.isHu();
		panNo = dbo.getPanNo();
		finishTime = dbo.getFinishTime();
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

}
