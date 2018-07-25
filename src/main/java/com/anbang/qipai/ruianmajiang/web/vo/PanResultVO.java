package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangPanPlayerResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.GamePlayerDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.PanResultDbo;

public class PanResultVO {

	private List<RuianMajiangPanPlayerResultVO> playerResultList;

	public PanResultVO(PanResultDbo dbo, Map<String, GamePlayerDbo> playerMap) {
		List<RuianMajiangPanPlayerResult> list = dbo.getPlayerResultList();
		playerResultList = new ArrayList<>(list.size());
		list.forEach((panPlayerResult) -> playerResultList
				.add(new RuianMajiangPanPlayerResultVO(playerMap.get(panPlayerResult.getPlayerId()), panPlayerResult)));
	}

	public List<RuianMajiangPanPlayerResultVO> getPlayerResultList() {
		return playerResultList;
	}

}
