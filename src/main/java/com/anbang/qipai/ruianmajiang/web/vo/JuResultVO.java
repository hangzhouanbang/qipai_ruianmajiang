package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.RuianMajiangJuResult;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.MajiangGamePlayerDbo;

public class JuResultVO {

	private String dayingjiaId;
	private String datuhaoId;
	private List<RuianMajiangJuPlayerResultVO> playerResultList;

	private PanResultVO lastPanResult;

	public JuResultVO(JuResultDbo juResultDbo, Map<String, MajiangGamePlayerDbo> playerMap) {
		RuianMajiangJuResult ruianMajiangJuResult = juResultDbo.getJuResult();
		dayingjiaId = ruianMajiangJuResult.getDayingjiaId();
		datuhaoId = ruianMajiangJuResult.getDatuhaoId();
		lastPanResult = new PanResultVO(juResultDbo.getLastPanResult(), playerMap);
		playerResultList = new ArrayList<>();
		ruianMajiangJuResult.getPlayerResultList().forEach((juPlayerResult) -> playerResultList
				.add(new RuianMajiangJuPlayerResultVO(juPlayerResult, playerMap.get(juPlayerResult.getPlayerId()))));
	}

}
