package com.anbang.qipai.ruianmajiang.plan.dao;

import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;

public interface PlayerInfoDao {

	PlayerInfo findById(String playerId);

}
