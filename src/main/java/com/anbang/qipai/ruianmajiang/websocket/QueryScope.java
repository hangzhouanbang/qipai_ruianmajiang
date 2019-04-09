package com.anbang.qipai.ruianmajiang.websocket;

import java.util.ArrayList;
import java.util.List;

import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.fpmpv.VotingWhenWaitingNextPan;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.multipan.player.PlayerPanFinished;
import com.dml.mpgame.game.extend.multipan.player.PlayerReadyToStartNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;

public enum QueryScope {
	gameInfo, panForMe, panResult, juResult, gameFinishVote;

	public static List<QueryScope> scopesForState(String gameState, String playerState) {
		List<QueryScope> scopes = new ArrayList<>();
		if (gameState.equals(WaitingStart.name)) {
			scopes.add(gameInfo);
		} else if (gameState.equals(Canceled.name)) {
			scopes.add(gameInfo);
		} else if (gameState.equals(Playing.name)) {
			scopes.add(gameInfo);
			scopes.add(panForMe);
		} else if (gameState.equals(VotingWhenPlaying.name)) {
			scopes.add(gameInfo);
			scopes.add(panForMe);
			scopes.add(gameFinishVote);
		} else if (gameState.equals(VoteNotPassWhenPlaying.name)) {
			scopes.add(QueryScope.gameInfo);
			scopes.add(QueryScope.gameFinishVote);
			scopes.add(QueryScope.panForMe);
		} else if (gameState.equals(FinishedByVote.name)) {
			scopes.add(juResult);
		} else if (gameState.equals(WaitingNextPan.name)) {
			if (playerState.equals(PlayerPanFinished.name)) {
				scopes.add(gameInfo);
				scopes.add(panResult);
			} else if (playerState.equals(PlayerReadyToStartNextPan.name)) {
				scopes.add(gameInfo);
			}
		} else if (gameState.equals(VotingWhenWaitingNextPan.name)) {
			scopes.add(gameInfo);
			scopes.add(gameFinishVote);
		} else if (gameState.equals(VoteNotPassWhenWaitingNextPan.name)) {
			scopes.add(QueryScope.gameFinishVote);
			scopes.add(QueryScope.gameInfo);
			if (playerState.equals(PlayerPanFinished.name)) {
				scopes.add(QueryScope.panResult);
			}
		} else if (gameState.equals(Finished.name)) {
			scopes.add(juResult);
		}
		return scopes;
	}

}
