package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;

/**
 * 统计拆字牌(带风牌)
 * 
 * @author Neo
 *
 */
public class CaizipaiListener
		implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerPengActionStatisticsListener {

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MajiangDaAction daAction, Ju ju) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateForNextPan() {
		// TODO Auto-generated method stub

	}

}
