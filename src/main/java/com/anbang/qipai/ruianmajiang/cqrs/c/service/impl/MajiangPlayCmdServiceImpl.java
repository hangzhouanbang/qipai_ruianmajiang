package com.anbang.qipai.ruianmajiang.cqrs.c.service.impl;

import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameManager;
import com.anbang.qipai.ruianmajiang.cqrs.c.domain.ReadyToNextPanResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.MajiangPlayCmdService;

@Component
public class MajiangPlayCmdServiceImpl extends CmdServiceBase implements MajiangPlayCmdService {

	@Override
	public MajiangActionResult action(String playerId, Integer actionId, Long actionTime) throws Exception {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		return majiangGameManager.majiangAction(playerId, actionId, actionTime);
	}

	@Override
	public ReadyToNextPanResult readyToNextPan(String playerId) throws Exception {
		MajiangGameManager majiangGameManager = singletonEntityRepository.getEntity(MajiangGameManager.class);
		return majiangGameManager.readyToNextPan(playerId);
	}

}
