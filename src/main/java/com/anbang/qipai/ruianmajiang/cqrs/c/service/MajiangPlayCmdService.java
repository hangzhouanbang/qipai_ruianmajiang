package com.anbang.qipai.ruianmajiang.cqrs.c.service;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;

public interface MajiangPlayCmdService {

	MajiangActionResult action(String playerId, Integer actionId) throws Exception;

}
