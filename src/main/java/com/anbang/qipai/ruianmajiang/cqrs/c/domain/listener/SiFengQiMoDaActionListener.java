package com.anbang.qipai.ruianmajiang.cqrs.c.domain.listener;

import com.dml.majiang.ju.Ju;
import com.dml.majiang.pai.MajiangPai;
import com.dml.majiang.pan.Pan;
import com.dml.majiang.player.MajiangPlayer;
import com.dml.majiang.player.action.da.MajiangDaAction;
import com.dml.majiang.player.action.listener.da.MajiangPlayerDaActionStatisticsListener;
import com.dml.majiang.player.action.listener.mo.MajiangPlayerMoActionStatisticsListener;
import com.dml.majiang.player.action.mo.MajiangMoAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 记录每个player的风牌数量
 * 用于记录胡的选手是否是四风齐
 */
public class SiFengQiMoDaActionListener implements MajiangPlayerDaActionStatisticsListener, MajiangPlayerMoActionStatisticsListener {

	private Map<String, Map<MajiangPai, Integer>> playerFengCounter = new HashMap<>();
	private Map<String, Boolean> firstMoPaiMap = new HashMap<>();

	private Map<MajiangPai, Integer> getOrCreateFengpaiCounter(final String playerId) {
		Map<MajiangPai, Integer> fengPaiCounter = this.playerFengCounter.get(playerId);
		if (fengPaiCounter == null) {
			fengPaiCounter = new HashMap<>();
			this.playerFengCounter.put(playerId, fengPaiCounter);
		}
		return fengPaiCounter;
	}

	private boolean isFengPai(MajiangPai pai) {
		return pai.ordinal() >= 27 && pai.ordinal() <= 30;
	}

	@Override
	public void update(MajiangDaAction daAction, Ju ju) {
		final MajiangPai pai = daAction.getPai();
		System.out.println(">>> 用户:"+daAction.getActionPlayerId()+", 打牌: "+pai);
		System.out.println(">>> da update 之前:"+string());
		//只记录风牌
		if (isFengPai(pai)) {
			final String playerId = daAction.getActionPlayerId();
			Map<MajiangPai, Integer> fengPaiCounter = this.getOrCreateFengpaiCounter(playerId);
			Integer counter = fengPaiCounter.get(pai);
			if (counter == null)
				fengPaiCounter.put(pai, 0);
			else
				fengPaiCounter.put(pai, --counter);
		}
		System.out.println(">>> da update 之后:"+string());
	}

	@Override
	public void updateForNextPan() {
		this.playerFengCounter.clear();
		this.firstMoPaiMap.clear();
	}

	//用户是不是第一次摸排
	private boolean isPlayerFistMo(String playerId){
		Boolean mo = this.firstMoPaiMap.putIfAbsent(playerId, Boolean.TRUE);
		return mo == null;
	}

	@Override
	public void update(MajiangMoAction moAction, Ju ju) {
		final String playerId = moAction.getActionPlayerId();
		final Pan currentPan = ju.getCurrentPan();
		final MajiangPlayer player = currentPan.findPlayerById(playerId);
		//第一次摸排，遍历初始牌，鬼牌
		if (isPlayerFistMo(playerId)){
		    //初始牌
			final List<MajiangPai> fangruShoupaiList = player.getFangruShoupaiList();
			for (MajiangPai pai : fangruShoupaiList) {
				//只记录风牌
				if (isFengPai(pai)) {
					this.moFengPai(pai,playerId);
				}
			}
			//鬼牌
			final List<MajiangPai> fangruGuiPaiList = player.getFangruGuipaiList();
            for (MajiangPai pai : fangruGuiPaiList) {
                //只记录风牌
                if (isFengPai(pai)) {
                    this.moFengPai(pai,playerId);
                }
            }
        }
		final MajiangPai pai = player.getGangmoShoupai();
		System.out.println(">>> 用户:"+moAction.getActionPlayerId()+", 摸牌: "+pai);
		System.out.println(">>> mo update 之前:"+string());
		//只记录风牌
		if (isFengPai(pai)) {
			this.moFengPai(pai,playerId);
		}
		System.out.println(">>> mo update 之后:"+string());
	}

	private void moFengPai(final MajiangPai pai,final String playerId){
		final Map<MajiangPai, Integer> fengPaiCounter = this.getOrCreateFengpaiCounter(playerId);
		Integer counter = fengPaiCounter.get(pai);
		if (counter == null) {
			fengPaiCounter.put(pai, 1);
		} else {
			fengPaiCounter.put(pai, ++counter);
		}
	}


	public boolean couldSiFengQi(final String playerId){
		final Map<MajiangPai, Integer> fengPaiCounter = this.getOrCreateFengpaiCounter(playerId);
		boolean siFengQi = true;
		for (Integer count : fengPaiCounter.values()) {
			if (count == 0) {
				siFengQi = false;
				break;
			}
		}
		System.out.println(">>> could siFengQi "+(siFengQi && (fengPaiCounter.size() == 4)));
		System.out.println(">>> count : " + string());
		return siFengQi && (fengPaiCounter.size() == 4);
	}

	private String string(){
		String result="";
		for (Map.Entry<String, Map<MajiangPai, Integer>> entry : playerFengCounter.entrySet()) {
			result += entry.getKey()+":";
			final Map<MajiangPai, Integer> paiMap = entry.getValue();
			for (Map.Entry<MajiangPai, Integer> e :paiMap.entrySet()){
				result += e.getKey()+"-"+e.getValue()+",";
			}
			result+=";";
		}
		return result;
	}


}
