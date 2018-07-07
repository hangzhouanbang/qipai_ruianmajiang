package com.anbang.qipai.ruianmajiang.cqrs.c.service.disruptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangActionResult;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.MajiangPlayCmdService;
import com.anbang.qipai.ruianmajiang.cqrs.c.service.impl.MajiangPlayCmdServiceImpl;
import com.highto.framework.concurrent.DeferredResult;
import com.highto.framework.ddd.CommonCommand;

@Component(value = "majiangPlayCmdService")
public class DisruptorMajiangPlayCmdService extends DisruptorCmdServiceBase implements MajiangPlayCmdService {

	@Autowired
	private MajiangPlayCmdServiceImpl majiangPlayCmdServiceImpl;

	@Override
	public MajiangActionResult action(String playerId, Integer actionId) throws Exception {
		CommonCommand cmd = new CommonCommand(MajiangPlayCmdServiceImpl.class.getName(), "action", playerId, actionId);
		DeferredResult<MajiangActionResult> result = publishEvent(disruptorFactory.getCoreCmdDisruptor(), cmd, () -> {
			MajiangActionResult majiangActionResult = majiangPlayCmdServiceImpl.action(cmd.getParameter(),
					cmd.getParameter());
			return majiangActionResult;
		});
		try {
			return result.getResult();
		} catch (Exception e) {
			throw e;
		}
	}

}
