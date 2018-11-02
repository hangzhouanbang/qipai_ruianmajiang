package com.anbang.qipai.ruianmajiang.plan.dao;

import com.anbang.qipai.ruianmajiang.plan.bean.MemberGoldBalance;

public interface MemberGoldBalanceDao {

	void save(MemberGoldBalance memberGoldBalance);

	MemberGoldBalance findByMemberId(String memberId);
}
