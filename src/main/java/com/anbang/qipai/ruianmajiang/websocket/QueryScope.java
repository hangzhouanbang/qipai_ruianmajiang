package com.anbang.qipai.ruianmajiang.websocket;

import java.util.ArrayList;
import java.util.List;

import com.dml.mpgame.game.Canceled;
import com.dml.mpgame.game.Finished;
import com.dml.mpgame.game.GameState;
import com.dml.mpgame.game.Playing;
import com.dml.mpgame.game.WaitingStart;
import com.dml.mpgame.game.extend.fpmpv.VoteNotPassWhenWaitingNextPan;
import com.dml.mpgame.game.extend.fpmpv.VotingWhenWaitingNextPan;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoted;
import com.dml.mpgame.game.extend.fpmpv.player.PlayerPanFinishedAndVoting;
import com.dml.mpgame.game.extend.multipan.WaitingNextPan;
import com.dml.mpgame.game.extend.multipan.player.PlayerPanFinished;
import com.dml.mpgame.game.extend.multipan.player.PlayerReadyToStartNextPan;
import com.dml.mpgame.game.extend.vote.FinishedByVote;
import com.dml.mpgame.game.extend.vote.VoteNotPassWhenPlaying;
import com.dml.mpgame.game.extend.vote.VotingWhenPlaying;
import com.dml.mpgame.game.player.GamePlayerState;

public enum QueryScope {
	gameInfo, panForMe, panResult, juResult, gameFinishVote;

	public static List<QueryScope> scopesForState(GameState gameState, GamePlayerState playerState) {
		List<QueryScope> scopes = new ArrayList<>();
		if (gameState.name().equals(WaitingStart.name)) {
			scopes.add(gameInfo);
		} else if (gameState.name().equals(Canceled.name)) {
			scopes.add(gameInfo);
		} else if (gameState.name().equals(Playing.name)) {
			scopes.add(gameInfo);
			scopes.add(panForMe);
		} else if (gameState.name().equals(VotingWhenPlaying.name)) {
			scopes.add(gameInfo);
			scopes.add(panForMe);
			scopes.add(gameFinishVote);
		} else if (gameState.name().equals(VoteNotPassWhenPlaying.name)) {
			scopes.add(QueryScope.gameInfo);
			scopes.add(QueryScope.gameFinishVote);
			scopes.add(QueryScope.panForMe);
		} else if (gameState.name().equals(FinishedByVote.name)) {
			scopes.add(juResult);
		} else if (gameState.name().equals(WaitingNextPan.name)) {
			if (playerState.name().equals(PlayerPanFinished.name)) {
				scopes.add(gameInfo);
				scopes.add(panResult);
			} else if (playerState.name().equals(PlayerReadyToStartNextPan.name)) {
				scopes.add(gameInfo);
			}
		} else if (gameState.name().equals(VotingWhenWaitingNextPan.name)) {
			scopes.add(gameInfo);
			scopes.add(gameFinishVote);
			if (playerState.name().equals(PlayerPanFinishedAndVoting.name)
					|| playerState.name().equals(PlayerPanFinishedAndVoted.name)) {
				scopes.add(QueryScope.panResult);
			}
		} else if (gameState.name().equals(VoteNotPassWhenWaitingNextPan.name)) {
			scopes.add(QueryScope.gameFinishVote);
			scopes.add(QueryScope.gameInfo);
			if (playerState.name().equals(PlayerPanFinishedAndVoting.name)
					|| playerState.name().equals(PlayerPanFinishedAndVoted.name)) {
				scopes.add(QueryScope.panResult);
			}
		} else if (gameState.name().equals(Finished.name)) {
			scopes.add(juResult);
		}
		return scopes;
	}

}
