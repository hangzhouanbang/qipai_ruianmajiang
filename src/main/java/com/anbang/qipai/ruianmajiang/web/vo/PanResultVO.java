package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;

public class PanResultVO {

	private List<RuianMajiangPanPlayerResultVO> playerResultList;

	private long finishTime;

	public PanResultVO(PanResultDbo dbo, Map<String, MajiangGamePlayerDbo> playerMap) {
		List<RuianMajiangPanPlayerResult> list = dbo.getPlayerResultList();
		playerResultList = new ArrayList<>(list.size());
		list.forEach((panPlayerResult) -> playerResultList
				.add(new RuianMajiangPanPlayerResultVO(playerMap.get(panPlayerResult.getPlayerId()),
						dbo.getZhuangPlayerId(), dbo.isZimo(), dbo.getDianpaoPlayerId(), panPlayerResult)));
		finishTime = dbo.getFinishTime();
	}

	public List<RuianMajiangPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

	public long getFinishTime() {
		return finishTime;
	}

}
