package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;
import com.dml.mpgame.game.extend.vote.GameFinishVoteValueObject;
import com.dml.mpgame.game.extend.vote.VoteOption;
import com.dml.mpgame.game.extend.vote.VoteResult;

public class GameFinishVoteValueObjectDbo implements ByteBufferAble {
	private String sponsorId;

	private List<String> votePlayerIds;

	private List<PlayerVoteDbo> playerIdVoteOptionList;

	private VoteResult result;

	private long startTime;

	private long endTime;

	public GameFinishVoteValueObjectDbo() {

	}

	public GameFinishVoteValueObjectDbo(GameFinishVoteValueObject gameFinishVoteValueObject) {
		sponsorId = gameFinishVoteValueObject.getSponsorId();
		votePlayerIds = new ArrayList<>(gameFinishVoteValueObject.getVotePlayerIds());
		Map<String, VoteOption> playerIdVoteOptionMap = gameFinishVoteValueObject.getPlayerIdVoteOptionMap();
		playerIdVoteOptionList = new ArrayList<>();
		for (String playerId : playerIdVoteOptionMap.keySet()) {
			PlayerVoteDbo playerVoteDbo = new PlayerVoteDbo();
			playerVoteDbo.setPlayerId(playerId);
			playerVoteDbo.setVote(playerIdVoteOptionMap.get(playerId));
			playerIdVoteOptionList.add(playerVoteDbo);
		}
		result = gameFinishVoteValueObject.getResult();
		startTime = gameFinishVoteValueObject.getStartTime();
		endTime = gameFinishVoteValueObject.getEndTime();
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public List<String> getVotePlayerIds() {
		return votePlayerIds;
	}

	public void setVotePlayerIds(List<String> votePlayerIds) {
		this.votePlayerIds = votePlayerIds;
	}

	public List<PlayerVoteDbo> getPlayerIdVoteOptionList() {
		return playerIdVoteOptionList;
	}

	public void setPlayerIdVoteOptionList(List<PlayerVoteDbo> playerIdVoteOptionList) {
		this.playerIdVoteOptionList = playerIdVoteOptionList;
	}

	public VoteResult getResult() {
		return result;
	}

	public void setResult(VoteResult result) {
		this.result = result;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(sponsorId, bb);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(votePlayerIds), bb);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(playerIdVoteOptionList), bb);
		if (result != null) {
			bb.put((byte) 1);
			bb.put((byte) result.ordinal());
		} else {
			bb.put((byte) 0);
		}
		bb.putLong(startTime);
		bb.putLong(endTime);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		sponsorId = ByteBufferSerializer.byteBufferToString(bb);
		votePlayerIds = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((playerId) -> {
			votePlayerIds.add((String) playerId);
		});
		playerIdVoteOptionList = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((playerVote) -> {
			playerIdVoteOptionList.add((PlayerVoteDbo) playerVote);
		});
		if (bb.get() == 1) {
			result = VoteResult.valueOf(bb.get());
		}
		startTime = bb.getLong();
		endTime = bb.getLong();
	}

}
