package com.jeffrey.project.poker.logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.player.Player;
import com.jeffrey.project.poker.rest.StateController;

@Component
public class GameStateManager {
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired
	GameState gameState;

	@Autowired
	ActionValidator actionValidator;

	List<Player> shovedAllInThisRound = new ArrayList<Player>();
	
	
	public void placeBet(String playerName, int token, double amount) throws Exception {
		// checks if it is a valid bet and returns player object for given name
		Player player = actionValidator.isValidBet(playerName, token, amount);
		double additionalAmount = player.placeBet(amount);
		gameState.addToPot(additionalAmount);
		gameState.setMostRecentBetSize(amount);
		gameState.setMostRecentActionReset(player);
		if(player.getChipCount() == 0) {
			player.setAllIn();
			shovedAllInThisRound.add(player);
		}
		postActionHandle(player);
	}

	public void makeCall(String playerName, int token, double amount) throws Exception {

		Player player = actionValidator.isValidCall(playerName, token, amount);
		double amountCalled = player.makeCall(amount, gameState.getMostRecentBetSize());
		gameState.addToPot(amountCalled);
		if(player.getChipCount() == 0) {
			player.setAllIn();
			shovedAllInThisRound.add(player);
		}
		postActionHandle(player);

	}

	public void check(String playerName, int token ) throws Exception{
		Player player = actionValidator.isValidCheck(playerName, token);
		player.setToChecked();
		postActionHandle(player);

	}

	public void fold(String playerName, int token ) throws Exception {
		Player player = actionValidator.isValidFold(playerName, token);
		player.setToFolded();
		postActionHandle(player);
	}
	
	private void setMaxCanReceive() {
		if(shovedAllInThisRound.size() != 0) {
			
			for(Player allInPlayer: shovedAllInThisRound) {
				double amountInThisRound = allInPlayer.getCurrAmountThisRound(); 
				double totalIncreaseFromRound = 0;
				double totalInThisRound = 0;
				
				for(Player player: gameState.getAllPlayers()) {
					totalInThisRound += player.getCurrAmountThisRound();
					if(player.getCurrAmountThisRound() > amountInThisRound) {
						totalIncreaseFromRound += amountInThisRound;
					} else {
						totalIncreaseFromRound += player.getCurrAmountThisRound();
					}
				}
				
				double maxCanEarnThisRound = gameState.getPot() - totalInThisRound + totalIncreaseFromRound;
				allInPlayer.setMaxCanEarnThisRound(maxCanEarnThisRound);
				allInPlayer.setAllIn();
			}
			
			shovedAllInThisRound = new ArrayList<Player>();
		}
	}
	
	private void setInactivePlayers() {
		for(Player player: gameState.getAllPlayers()) {
			if(player.isAllIn() && player.getChipCount() > 0) {
				player.setToWaiting();
			}
			if(player.isActive()) {
				if(player.getChipCount() == 0) {
					player.setInactive();
				}
			}
		}
	}

	public boolean postActionHandle(Player prevPlayer) throws Exception {

		/*
		 * this method will change the status of the next player before entering this,
		 * must set status of player
		 */
		Player nextPlayer = prevPlayer.getNext();
		
		if(nextPlayer.getNextInPlay() == nextPlayer) {
			gameState.dealRestOfCards();
			gameState.setRunStatus("end");
			gameState.distributeMoneyOneRemainingActive();
			setInactivePlayers();
			endOfRoundHandle();
			return true;
		}
		
		if (gameState.getMostRecentActionReset() == nextPlayer) {
			gameState.setMostRecentActionReset(gameState.getPlayersList().getDealer().getNextInPlay());
			
			setMaxCanReceive();
			endOfRoundHandle();
			return true;
		}
		switch (nextPlayer.getStatus()) {
			case "action":
				break;
			case "waiting":
				nextPlayer.setToAction();
				gameState.setCurrTurn(nextPlayer);
				break;
			case "checked": 
				nextPlayer.setToAction();
				gameState.setCurrTurn(nextPlayer);
				break;
			case "folded":
				postActionHandle(nextPlayer);
				break;
			case "bet":
				nextPlayer.setToAction();
				gameState.setCurrTurn(nextPlayer);
				break;
			case "called":
				nextPlayer.setToAction();
				gameState.setCurrTurn(nextPlayer);
				break;
			case "bigBlind":
				nextPlayer.setToAction();
				gameState.setCurrTurn(nextPlayer);
				break;
			case "allIn":
				postActionHandle(nextPlayer);
				break;
		}

		return false;
	}
	
	private void endOfRoundHandle() throws Exception {
		switch(gameState.getRunStatus()) {
			case "preFlop": 
				gameState.dealFlop();
				gameState.setRunStatus("flop");
				resetBeforeNextRoundOfBetting();
				break; 
			case "flop": 
				gameState.dealTurn();
				gameState.setRunStatus("turn");
				resetBeforeNextRoundOfBetting();
				break; 
			case "turn": 
				gameState.dealRiver();
				gameState.setRunStatus("river");
				resetBeforeNextRoundOfBetting();
				break; 
			case "river": 
				gameState.setRunStatus("end");
				gameState.distributeMoneyToWinners();
			case "end": 
				gameState.getPlayersList().setDealer(gameState.getPlayersList().getDealer().getNextActive());
				//gameState.startHand();
				break;
		}
	}
	
	private void resetBeforeNextRoundOfBetting() throws Exception {
		List<Player> currPlayers = gameState.getPlayersList().getAllPlayersInList();
		for(Player player: currPlayers) {
			player.setCurrAmountThisRound(0);
		}
		gameState.setMostRecentBetSize(0);
		setActionForNextRound(); 
	}
	
	private void setActionForNextRound() throws Exception {
		Player currPlayer = gameState.getPlayersList().getDealer().getNextInPlay();
		gameState.setCurrTurn(currPlayer);
		boolean isAction = false;
		do {
			if(currPlayer.inHand()){
				if(!isAction) {
					currPlayer.setToAction();
					isAction = true;	
				} else {
					currPlayer.setToWaiting();
				}
			}
			currPlayer = currPlayer.getNext();
		} while(currPlayer != gameState.getPlayersList().getDealer().getNextInPlay());
	}

}
