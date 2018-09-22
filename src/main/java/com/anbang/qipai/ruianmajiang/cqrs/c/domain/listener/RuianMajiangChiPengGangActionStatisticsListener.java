package com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.HuFirstException;
import com.dml.majiang.player.action.MajiangPlayerAction;
import com.dml.majiang.player.action.MajiangPlayerActionType;
import com.dml.majiang.player.action.chi.MajiangChiAction;
import com.dml.majiang.player.action.chi.PengganghuFirstException;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.gang.MajiangGangAction;
import com.dml.majiang.player.action.listener.chi.MajiangPlayerChiActionStatisticsListener;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.gang.MajiangPlayerGangActionStatisticsListener;
import com.dml.majiang.player.action.listener.peng.MajiangPlayerPengActionStatisticsListener;
import com.dml.majiang.player.action.peng.MajiangPengAction;

/**
 * 瑞安麻将统计器，包括绝张统计、吃碰杠同时出现时记录动作、总杠数统计
 * 
 * @author lsc
 *
 */
public class RuianMajiangChiPengGangActionStatisticsListener
		implements MajiangPlayerChiActionStatisticsListener, MajiangPlayerPengActionStatisticsListener,
		MajiangPlayerGangActionStatisticsListener, MajiangPlayerDaActionStatisticsListener {

	private Map<String, MajiangPlayerAction> playerActionMap = new HashMap<>();

	private int[] mingpaiCountArray = new int[MajiangPai.values().length];

	private int count = 0;

	@Override
	public void updateForNextPan() {
		playerActionMap = new HashMap<>();
		Arrays.fill(mingpaiCountArray, 0);
		count = 0;
	}

	// 清空当前轮动作
	public void updateForNextLun() {
		playerActionMap.clear();
	}

	@Override
	public void update(MajiangGangAction gangAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(gangAction.getActionPlayerId());
		MajiangPlayer xiajia = currentPan.findXiajia(player);
		while (true) {
			if (!xiajia.getId().equals(player.getId())) {
				Set<MajiangPlayerActionType> actionTypesSet = xiajia.collectActionCandidatesType();
				if (actionTypesSet.contains(MajiangPlayerActionType.hu)) {
					playerActionMap.put(player.getId(), gangAction);
					player.clearActionCandidates();// 玩家已经做了决定，要删除动作
					throw new HuFirstException();
				}
			} else {
				break;
			}
			xiajia = currentPan.findXiajia(xiajia);
		}
		count++;
	}

	@Override
	public void update(MajiangPengAction pengAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(pengAction.getActionPlayerId());
		MajiangPlayer xiajia = currentPan.findXiajia(player);
		while (true) {
			if (!xiajia.getId().equals(player.getId())) {
				Set<MajiangPlayerActionType> actionTypesSet = xiajia.collectActionCandidatesType();
				if (actionTypesSet.contains(MajiangPlayerActionType.hu)) {
					playerActionMap.put(player.getId(), pengAction);
					player.clearActionCandidates();// 玩家已经做了决定，要删除动作
					throw new HuFirstException();
				}
			} else {
				break;
			}
			xiajia = currentPan.findXiajia(xiajia);
		}
		mingpaiCountArray[pengAction.getPai().ordinal()] += 2;
	}

	@Override
	public void update(MajiangChiAction chiAction, Ju ju) throws Exception {
		Pan currentPan = ju.getCurrentPan();
		MajiangPlayer player = currentPan.findPlayerById(chiAction.getActionPlayerId());
		MajiangPlayer xiajia = currentPan.findXiajia(player);
		while (true) {
			if (!xiajia.getId().equals(player.getId())) {
				Set<MajiangPlayerActionType> actionTypesSet = xiajia.collectActionCandidatesType();
				if (actionTypesSet.contains(MajiangPlayerActionType.peng)
						|| actionTypesSet.contains(MajiangPlayerActionType.gang)
						|| actionTypesSet.contains(MajiangPlayerActionType.hu)) {
					playerActionMap.put(player.getId(), chiAction);
					player.clearActionCandidates();// 玩家已经做了决定，要删除动作
					throw new HuFirstException();
				}
			} else {
				break;
			}
			xiajia = currentPan.findXiajia(xiajia);
		}
		for (MajiangPlayerAction recordAction : playerActionMap.values()) {
			if (recordAction.getType().equals(MajiangPlayerActionType.peng)
					|| recordAction.getType().equals(MajiangPlayerActionType.gang)) {
				playerActionMap.put(player.getId(), chiAction);
				player.clearActionCandidates();// 玩家已经做了决定，要删除动作
				throw new PengganghuFirstException();
			}
		}
	}

	public MajiangPlayerAction findPlayerFinallyDoneAction() {
		if (playerActionMap.isEmpty()) {
			return null;
		}
		for (MajiangPlayerAction action : playerActionMap.values()) {
			if (action.getType().equals(MajiangPlayerActionType.gang)) {
				return action;
			} else if (action.getType().equals(MajiangPlayerActionType.peng)) {
				return action;
			} else if (action.getType().equals(MajiangPlayerActionType.chi)) {
				return action;
			} else {
				return null;
			}
		}
		return null;
	}

	public boolean ifJuezhang(MajiangPai pai) {
		return mingpaiCountArray[pai.ordinal()] == 3;
	}

	public boolean ifMingPai(MajiangPai pai) {
		return mingpaiCountArray[pai.ordinal()] > 0;
	}

	@Override
	public void update(MajiangDaAction daAction, Ju ju) throws Exception {
		mingpaiCountArray[daAction.getPai().ordinal()]++;
	}

	public Map<String, MajiangPlayerAction> getPlayerActionMap() {
		return playerActionMap;
	}

	public void setPlayerActionMap(Map<String, MajiangPlayerAction> playerActionMap) {
		this.playerActionMap = playerActionMap;
	}

	public int[] getMingpaiCountArray() {
		return mingpaiCountArray;
	}

	public void setMingpaiCountArray(int[] mingpaiCountArray) {
		this.mingpaiCountArray = mingpaiCountArray;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
