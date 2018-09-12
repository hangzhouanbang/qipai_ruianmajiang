package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.ju.result.JuResult;
import com.dml.majiang.ju.result.JuResultBuilder;
import com.dml.majiang.pan.result.PanResult;

public class RuianMajiangJuResultBuilder implements JuResultBuilder {

	@Override
	public JuResult buildJuResult(Ju ju) {
		RuianMajiangJuResult ruianMajiangJuResult = new RuianMajiangJuResult();
		ruianMajiangJuResult.setFinishedPanCount(ju.countFinishedPan());
		if (ju.countFinishedPan() > 0) {
			Map<String, RuianMajiangJuPlayerResult> juPlayerResultMap = new HashMap<>();
			for (PanResult panResult : ju.getFinishedPanResultList()) {
				RuianMajiangPanResult ruianMajiangPanResult = (RuianMajiangPanResult) panResult;
				for (RuianMajiangPanPlayerResult panPlayerResult : ruianMajiangPanResult.getPanPlayerResultList()) {
					RuianMajiangJuPlayerResult juPlayerResult = juPlayerResultMap.get(panPlayerResult.getPlayerId());
					if (juPlayerResult == null) {
						juPlayerResult = new RuianMajiangJuPlayerResult();
						juPlayerResult.setPlayerId(panPlayerResult.getPlayerId());
						juPlayerResultMap.put(panPlayerResult.getPlayerId(), juPlayerResult);
					}
					if (ruianMajiangPanResult.ifPlayerHu(panPlayerResult.getPlayerId())) {
						juPlayerResult.increaseHuCount();
					}
					juPlayerResult.increaseCaishenCount(
							ruianMajiangPanResult.playerGuipaiCount(panPlayerResult.getPlayerId()));
					if (panPlayerResult.getScore().getPao() != null) {
						juPlayerResult.increaseDapaoCount(panPlayerResult.getScore().getPao().getValue());
					}
					juPlayerResult.tryAndUpdateMaxHushu(panPlayerResult.getScore().getHushu().getValue());
					juPlayerResult.increaseTotalScore(panPlayerResult.getTotalScore());
				}
			}

			RuianMajiangJuPlayerResult dayingjia = null;
			RuianMajiangJuPlayerResult datuhao = null;
			for (RuianMajiangJuPlayerResult juPlayerResult : juPlayerResultMap.values()) {
				if (dayingjia == null) {
					dayingjia = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() > dayingjia.getTotalScore()) {
						dayingjia = juPlayerResult;
					}
				}

				if (datuhao == null) {
					datuhao = juPlayerResult;
				} else {
					if (juPlayerResult.getTotalScore() < datuhao.getTotalScore()) {
						datuhao = juPlayerResult;
					}
				}
			}
			ruianMajiangJuResult.setDatuhaoId(datuhao.getPlayerId());
			ruianMajiangJuResult.setDayingjiaId(dayingjia.getPlayerId());
			ruianMajiangJuResult.setPlayerResultList(new ArrayList<>(juPlayerResultMap.values()));
		}
		return ruianMajiangJuResult;
	}

}
