package com.anbang.qipai.ruianmajiang.cqrs.q.dbo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.anbang.qipai.ruianmajiang.cqrs.c.domain.MajiangGameValueObject;
import com.anbang.qipai.ruianmajiang.plan.bean.PlayerInfo;
import com.dml.majiang.serializer.ByteBufferAble;
import com.dml.majiang.serializer.ByteBufferSerializer;
import com.dml.mpgame.game.GamePlayerValueObject;

public class MajiangGameDbo implements ByteBufferAble {
	private String id;
	private int difen;
	private int taishu;
	private int panshu;
	private int renshu;
	private boolean dapao;
	private int panNo;
	private String state;// 原来是 waitingStart, playing, waitingNextPan, finished
	private List<MajiangGamePlayerDbo> players;
	private List<String> xipaiPlayerIds;

	public byte[] toByteArray(int bufferSize) throws Throwable {
		byte[] buffer = new byte[bufferSize];
		ByteBuffer bb = ByteBuffer.wrap(buffer);
		ByteBufferSerializer.objToByteBuffer(this, bb);
		byte[] byteArray = new byte[bb.position()];
		System.arraycopy(buffer, 0, byteArray, 0, byteArray.length);
		return byteArray;
	}

	public static MajiangGameDbo fromByteArray(byte[] byteArray) {
		try {
			return ByteBufferSerializer.byteBufferToObj(ByteBuffer.wrap(byteArray));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void toByteBuffer(ByteBuffer bb) throws Throwable {
		ByteBufferSerializer.stringToByteBuffer(id, bb);
		bb.putInt(difen);
		bb.putInt(taishu);
		bb.putInt(panshu);
		bb.putInt(renshu);
		ByteBufferSerializer.booleanToByteBuffer(dapao, bb);
		bb.putInt(panNo);
		ByteBufferSerializer.stringToByteBuffer(state, bb);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(players), bb);
		ByteBufferSerializer.listToByteBuffer(new ArrayList<>(xipaiPlayerIds), bb);
	}

	@Override
	public void fillByByteBuffer(ByteBuffer bb) throws Throwable {
		id = ByteBufferSerializer.byteBufferToString(bb);
		difen = bb.getInt();
		taishu = bb.getInt();
		panshu = bb.getInt();
		renshu = bb.getInt();
		dapao = ByteBufferSerializer.byteBufferToBoolean(bb);
		panNo = bb.getInt();
		state = ByteBufferSerializer.byteBufferToString(bb);
		players = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((player) -> {
			players.add((MajiangGamePlayerDbo) player);
		});
		xipaiPlayerIds = new ArrayList<>();
		ByteBufferSerializer.byteBufferToList(bb).forEach((playerId) -> {
			xipaiPlayerIds.add((String) playerId);
		});
	}

	public MajiangGameDbo() {
	}

	public MajiangGameDbo(MajiangGameValueObject majiangGame, Map<String, PlayerInfo> playerInfoMap) {
		id = majiangGame.getId();
		difen = majiangGame.getDifen();
		taishu = majiangGame.getTaishu();
		panshu = majiangGame.getPanshu();
		renshu = majiangGame.getRenshu();
		dapao = majiangGame.isDapao();
		panNo = majiangGame.getPanNo();
		state = majiangGame.getState().name();
		xipaiPlayerIds = new ArrayList<>(majiangGame.getXipaiPlayerIds());
		players = new ArrayList<>();
		Map<String, Integer> playeTotalScoreMap = majiangGame.getPlayeTotalScoreMap();
		for (GamePlayerValueObject playerValueObject : majiangGame.getPlayers()) {
			String playerId = playerValueObject.getId();
			PlayerInfo playerInfo = playerInfoMap.get(playerId);
			MajiangGamePlayerDbo playerDbo = new MajiangGamePlayerDbo();
			playerDbo.setHeadimgurl(playerInfo.getHeadimgurl());
			playerDbo.setNickname(playerInfo.getNickname());
			playerDbo.setGender(playerInfo.getGender());
			playerDbo.setOnlineState(playerValueObject.getOnlineState());
			playerDbo.setPlayerId(playerId);
			playerDbo.setState(playerValueObject.getState().name());
			if (playeTotalScoreMap.get(playerId) != null) {
				playerDbo.setTotalScore(playeTotalScoreMap.get(playerId));
			}
			players.add(playerDbo);
		}

	}

	public MajiangGamePlayerDbo findPlayer(String playerId) {
		for (MajiangGamePlayerDbo player : players) {
			if (player.getPlayerId().equals(playerId)) {
				return player;
			}
		}
		return null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getDifen() {
		return difen;
	}

	public void setDifen(int difen) {
		this.difen = difen;
	}

	public int getTaishu() {
		return taishu;
	}

	public void setTaishu(int taishu) {
		this.taishu = taishu;
	}

	public int getPanshu() {
		return panshu;
	}

	public void setPanshu(int panshu) {
		this.panshu = panshu;
	}

	public int getRenshu() {
		return renshu;
	}

	public void setRenshu(int renshu) {
		this.renshu = renshu;
	}

	public boolean isDapao() {
		return dapao;
	}

	public void setDapao(boolean dapao) {
		this.dapao = dapao;
	}

	public int getPanNo() {
		return panNo;
	}

	public void setPanNo(int panNo) {
		this.panNo = panNo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<MajiangGamePlayerDbo> getPlayers() {
		return players;
	}

	public void setPlayers(List<MajiangGamePlayerDbo> players) {
		this.players = players;
	}

	public List<String> getXipaiPlayerIds() {
		return xipaiPlayerIds;
	}

	public void setXipaiPlayerIds(List<String> xipaiPlayerIds) {
		this.xipaiPlayerIds = xipaiPlayerIds;
	}

}
