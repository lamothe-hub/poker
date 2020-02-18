package com.jeffrey.project.card.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.GameState;
import com.jeffrey.project.card.model.player.Player;

@Component
public class GameStateManager {

	@Autowired 
	GameState gameState;
	
	@Autowired 
	ActionValidator actionValidator;
	
	public void placeBet(String playerName, double amount) {
		try {
			// checks if it is a valid bet and returns player object for given name
			Player player = actionValidator.isValidBet(playerName, amount);
			double additionalAmount = player.placeBet(amount);
			gameState.addToPot(additionalAmount);
			gameState.setMostRecentBetSize(amount);
			gameState.setMostRecentActionReset(player);
			postActionHandle(player);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void makeCall(String playerName, double amount) {
		try {
			Player player = actionValidator.isValidCall(playerName, amount); 
			double amountCalled = player.makeCall(amount, gameState.getMostRecentBetSize());
			gameState.addToPot(amountCalled);
			postActionHandle(player);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void check(String playerName) {
		try {
			Player player = actionValidator.isValidCheck(playerName); 
			player.setToChecked();
			postActionHandle(player);
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void fold(String playerName) {
		try {
			Player player = actionValidator.isValidFold(playerName); 
			player.setToFolded(); 
			postActionHandle(player); 
		} catch(Exception ex) {
			System.out.println(ex);
		}
	}
	
	public boolean postActionHandle(Player prevPlayer) {
		
		/*
		 * this method will change the status of the next player
		 * before entering this, must set status of player
		 */
		Player nextPlayer = prevPlayer.getNext();
		if(gameState.getMostRecentActionReset() == nextPlayer) {
			System.out.println("THE ACTION FOR ThIS ROUND IS OVER!!");
			return true; 
		} 
		switch(nextPlayer.getStatus()) {
		case "action": 
			break; 
		case "waiting": 
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
	
	
}
