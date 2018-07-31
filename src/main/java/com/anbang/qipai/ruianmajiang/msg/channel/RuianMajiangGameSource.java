package com.anbang.qipai.ruianmajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface RuianMajiangGameSource {
	@Output
	MessageChannel ruianMajiangGame();
}
