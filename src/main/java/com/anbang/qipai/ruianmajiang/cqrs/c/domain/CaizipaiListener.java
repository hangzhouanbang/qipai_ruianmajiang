package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import com.dml.majiang.action.MajiangDaAction;
import com.dml.majiang.action.MajiangPengAction;
import com.dml.majiang.action.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.action.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.ju.Ju;

/**
 * 统计拆字牌
 * 
 * @author Neo
 *
 */
public class CaizipaiListener
		implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerPengActionStatisticsListener {

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(MajiangDaAction daAction, Ju ju) throws Exception {
		// TODO Auto-generated method stub

	}

}
