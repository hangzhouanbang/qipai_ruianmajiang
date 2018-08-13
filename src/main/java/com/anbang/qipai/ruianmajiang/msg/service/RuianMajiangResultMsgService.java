package com.anbang.qipai.ruianmajiang.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.ruianmajiang.msg.channel.RuianMajiangResultSource;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;
import com.anbang.qipai.ruianmajiang.web.vo.JuResultVO;

@EnableBinding(RuianMajiangResultSource.class)
public class RuianMajiangResultMsgService {

	@Autowired
	private RuianMajiangResultSource ruianMajiangResultSource;

	public void recordJuResult(JuResultVO juResult) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ruianmajiang ju result");
		mo.setData(juResult);
		ruianMajiangResultSource.ruianMajiangResult().send(MessageBuilder.withPayload(mo).build());
	}
}
