package com.anbang.qipai.ruianmajiang.web.vo;

import java.util.ArrayList;
import java.util.List;

import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pai.valueobj.PaiListValueObject;
import com.dml.majiang.pan.cursor.PaiCursor;
import com.dml.majiang.pan.frame.PanValueObject;

public class PanValueObjectVO {

	/**
	 * 编号，代表一局中的第几盘
	 */
	private int no;

	private List<MajiangPlayerValueObjectVO> playerList;

	private String zhuangPlayerId;

	private PaiListValueObject avaliablePaiList;

	/**
	 * 公示的鬼牌集合,不能行牌
	 */
	private List<MajiangPai> publicGuipaiList;

	/**
	 * 给用户看得到的等待箭头，实际等的不一定是他
	 */
	private String publicWaitingPlayerId;

	/**
	 * 当前活跃的那张牌的定位
	 */
	private PaiCursor activePaiCursor;

	/**
	 * 流局警告
	 */
	private boolean liujuWarning;

	public PanValueObjectVO(PanValueObject panValueObject) {
		no = panValueObject.getNo();
		zhuangPlayerId = panValueObject.getZhuangPlayerId();
		avaliablePaiList = panValueObject.getAvaliablePaiList();
		publicGuipaiList = panValueObject.getPublicGuipaiList();
		publicWaitingPlayerId = panValueObject.getPublicWaitingPlayerId();
		activePaiCursor = panValueObject.getActivePaiCursor();
		playerList = new ArrayList<>();
		panValueObject.getPlayerList()
				.forEach((playerValueObject) -> playerList.add(new MajiangPlayerValueObjectVO(playerValueObject)));
		int liupai = 15;
		int gangCount = 0;
		for (MajiangPlayerValueObjectVO player : playerList) {
			gangCount += player.getGangchupaiZuList().size();
		}
		if (gangCount > 0) {
			liupai += (4 + (gangCount - 1) * 2);
		}
		if ((avaliablePaiList.getPaiCount() - liupai) < playerList.size()) {// 进入流局前最后4张
			liujuWarning = true;
		}
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public List<MajiangPlayerValueObjectVO> getPlayerList() {
		return playerList;
	}

	public void setPlayerList(List<MajiangPlayerValueObjectVO> playerList) {
		this.playerList = playerList;
	}

	public String getZhuangPlayerId() {
		return zhuangPlayerId;
	}

	public void setZhuangPlayerId(String zhuangPlayerId) {
		this.zhuangPlayerId = zhuangPlayerId;
	}

	public PaiListValueObject getAvaliablePaiList() {
		return avaliablePaiList;
	}

	public void setAvaliablePaiList(PaiListValueObject avaliablePaiList) {
		this.avaliablePaiList = avaliablePaiList;
	}

	public List<MajiangPai> getPublicGuipaiList() {
		return publicGuipaiList;
	}

	public void setPublicGuipaiList(List<MajiangPai> publicGuipaiList) {
		this.publicGuipaiList = publicGuipaiList;
	}

	public String getPublicWaitingPlayerId() {
		return publicWaitingPlayerId;
	}

	public void setPublicWaitingPlayerId(String publicWaitingPlayerId) {
		this.publicWaitingPlayerId = publicWaitingPlayerId;
	}

	public PaiCursor getActivePaiCursor() {
		return activePaiCursor;
	}

	public void setActivePaiCursor(PaiCursor activePaiCursor) {
		this.activePaiCursor = activePaiCursor;
	}

	public boolean isLiujuWarning() {
		return liujuWarning;
	}

	public void setLiujuWarning(boolean liujuWarning) {
		this.liujuWarning = liujuWarning;
	}

}
