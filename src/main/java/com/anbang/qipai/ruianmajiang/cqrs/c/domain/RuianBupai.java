package com.anbang.qipai.ruianmajiang.cqrs.c.domain;

import java.nio.ByteBuffer;

import com.dml.majiang.player.action.mo.MopaiReason;

public class RuianBupai implements MopaiReason {

	public static final String name = "bupai";

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
	}

	@Override
	public String getName() {
		return name;
	}

}
