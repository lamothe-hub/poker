package com.jeffrey.project.card.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.GameState;
import com.jeffrey.project.card.model.player.Player;

@Component
public class ActionValidator {

	/* 
	 * whereas GameState component keeps track of the state and offers
	 * methods to manipulate the data, GameStateManager is called by the 
	 * controller methods to make sure that the requested changes are 
	 * valid (i.e. it is the proper player's turn; the player has enough 
	 * chips to make that bet size; etc.)
	 */
	
	
	@Autowired
	GameState gameState;
	
	
	public String isValidBet(String playerName, double amount) {
		// check if it is that player's turn and if it is a valid betsize 
		Player player = gameState.getPlayersList().getPlayerByName(playerName);
		if(player == null) {
			return "Player '" + playerName + "' does not exist";
		}
		String message = "";
		if(!isPlayersTurn(player)) {
			message = "It is not " + player.getName() + "'s turn. ";
		} else if(!isValidBetSize(player, amount)) {
			message += amount + " is not a valid bet size. "; 
		}
		
		return message;
	}
	
	public String isValidCall(String playerName, double amount) {
		Player player = gameState.getPlayersList().getPlayerByName(playerName); 
		if(player == null) {
			return "Player '" + playerName + "' does not exist";
		}
		String message = ""; 
		if(!isPlayersTurn(player)) {
			message = "It is not " + player.getName() + "'s turn. ";
		} else if(!isValidCallSize(player, amount)) {
			message += amount + " is not a valid call size. "; 
		}
		
		return message;
	}
	
	public String isValidCheck(String playerName) {
		String message = "";
		
		Player player = gameState.getPlayersList().getPlayerByName(playerName); 
		if(player == null) {
			return "Player '" + playerName + "' does not exist";
		}
		if(!isPlayersTurn(player)) {
			message = "It is not " + player.getName() + "'s turn. ";
		} else if(!alreadyBetOrCalled(player)) {
			message = "Player '" + playerName + "' is not allowed to check here";
		}
		
		return message;
	}
	
	public boolean alreadyBetOrCalled(Player player) {
		if(player.getCurrAmountThisRound() == gameState.getMostRecentBetSize()) {
			return true;
		}
		return false;
	}
	
	public boolean isValidCallSize(Player player, double amount) {
		if(amount <= player.getChipCount()) {
			if(amount == gameState.getMostRecentBetSize() - player.getCurrAmountThisRound()) {
				return true; 
			} else if(amount == player.getChipCount() - player.getCurrAmountThisRound()) {
				return true;
			}	
		}
		return false;
	}
	
	public boolean isPlayersTurn(Player player) {
		if(gameState.getCurrTurn().getName().equals(player.getName())) {
			return true;
		}
		return false;
	}
	
	public boolean isValidBetSize(Player player, double amount) {
		if(amount >= gameState.getBigBlind() + gameState.getMostRecentBetSize()) {
			return true;
		} else if(amount == player.getChipCount()) {
			return true;
		} 
		return false;
	}
	
	
	
}
