package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.util.List;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.result.PanResult;
import com.dml.majiang.player.menfeng.PlayersMenFengDeterminer;
import com.dml.majiang.position.MajiangPosition;
import com.dml.majiang.position.MajiangPositionUtil;

public class RuianMajiangPlayersMenFengDeterminer implements PlayersMenFengDeterminer {

	private String zhuangPlayerId;

	@Override
	public void determinePlayersMenFeng(Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		PanResult latestFinishedPanResult = ju.findLatestFinishedPanResult();
		String zhuangPlayerId = latestFinishedPanResult.findZhuangPlayerId();
		this.zhuangPlayerId = zhuangPlayerId;
		if (!latestFinishedPanResult.hasHu()) {
			if (latestFinishedPanResult.hasGang()) {
				// 先找出庄的下家
				String zhuangXiajiaPlayerId = latestFinishedPanResult.findXiajiaPlayerId(zhuangPlayerId);
				this.zhuangPlayerId = zhuangXiajiaPlayerId;
				// 再计算要顺时针移几步到东
				MajiangPosition p = latestFinishedPanResult.playerMenFeng(zhuangXiajiaPlayerId);
				int n = 0;
				while (true) {
					MajiangPosition np = MajiangPositionUtil.nextPositionClockwise(p);
					n++;
					if (np.equals(MajiangPosition.dong)) {
						break;
					} else {
						p = np;
					}
				}
				// 最后给所有玩家设置门风
				List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
				for (String playerId : allPlayerIds) {
					MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
					MajiangPosition newPlayerMenFeng = playerMenFeng;
					for (int i = 0; i < n; i++) {
						newPlayerMenFeng = MajiangPositionUtil.nextPositionClockwise(newPlayerMenFeng);
					}
					currentPan.updatePlayerMenFeng(playerId, newPlayerMenFeng);
				}
			} else {
				List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
				for (String playerId : allPlayerIds) {
					MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
					currentPan.updatePlayerMenFeng(playerId, playerMenFeng);
				}
			}
		} else if (!latestFinishedPanResult.ifPlayerHu(zhuangPlayerId)) {// 庄没有胡
			// 先找出庄的下家
			String zhuangXiajiaPlayerId = latestFinishedPanResult.findXiajiaPlayerId(zhuangPlayerId);
			this.zhuangPlayerId = zhuangXiajiaPlayerId;
			// 再计算要顺时针移几步到东
			MajiangPosition p = latestFinishedPanResult.playerMenFeng(zhuangXiajiaPlayerId);
			int n = 0;
			while (true) {
				MajiangPosition np = MajiangPositionUtil.nextPositionClockwise(p);
				n++;
				if (np.equals(MajiangPosition.dong)) {
					break;
				} else {
					p = np;
				}
			}
			// 最后给所有玩家设置门风
			List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
			for (String playerId : allPlayerIds) {
				MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
				MajiangPosition newPlayerMenFeng = playerMenFeng;
				for (int i = 0; i < n; i++) {
					newPlayerMenFeng = MajiangPositionUtil.nextPositionClockwise(newPlayerMenFeng);
				}
				currentPan.updatePlayerMenFeng(playerId, newPlayerMenFeng);
			}
		} else {
			List<String> allPlayerIds = latestFinishedPanResult.allPlayerIds();
			for (String playerId : allPlayerIds) {
				MajiangPosition playerMenFeng = latestFinishedPanResult.playerMenFeng(playerId);
				currentPan.updatePlayerMenFeng(playerId, playerMenFeng);
			}
		}
	}

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
	}

}
