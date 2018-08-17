package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.pan.finish.CurrentPanFinishiDeterminer;
import com.dml.majiang.player.action.listener.gang.GangCounter;

public class RuianMajiangPanFinishiDeterminer implements CurrentPanFinishiDeterminer {

	@Override
	public boolean determineToFinishCurrentPan(Ju ju) {
		Pan currentPan = ju.getCurrentPan();
		boolean hu = currentPan.anyPlayerHu();
		if (hu) {
			return true;
		} else {
			int liupai = 14;
			GangCounter gangCounter = ju.getActionStatisticsListenerManager().findListener(GangCounter.class);
			if (gangCounter.getCount() > 0) {
				liupai += (4 + (gangCounter.getCount() - 1) * 2);
			}

			int avaliablePaiLeft = currentPan.countAvaliablePai();

			if (avaliablePaiLeft <= liupai) {
				return true;
			} else {
				return false;
			}

		}

	}

}
