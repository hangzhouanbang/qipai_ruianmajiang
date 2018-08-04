package com.anbang.qipai.ruianmajiang.msg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.anbang.qipai.ruianmajiang.cqrs.q.dbo.JuResultDbo;
import com.anbang.qipai.ruianmajiang.msg.channel.RuianMajiangJuResultSource;
import com.anbang.qipai.ruianmajiang.msg.msjobj.CommonMO;

@EnableBinding(RuianMajiangJuResultSource.class)
public class RuianMajiangJuResultMsgService {

	@Autowired
	private RuianMajiangJuResultSource ruianMajiangJuResultSource;

	public void recordJuResult(JuResultDbo dbo) {
		CommonMO mo = new CommonMO();
		mo.setMsg("ruianmajiang juresult");
		mo.setData(dbo);
		ruianMajiangJuResultSource.ruianMajiangJuResult().send(MessageBuilder.withPayload(mo).build());
	}
}
