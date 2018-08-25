package com.anbang.qipai.ruianmajiang.msg.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface GameRoomSink {

	String RUIANGAMEROOM = "ruianGameRoom";

	@Input
	SubscribableChannel ruianGameRoom();
}
