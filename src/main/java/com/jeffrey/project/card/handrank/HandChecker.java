package com.jeffrey.project.card.handrank;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.GameState;
import com.jeffrey.project.card.model.card.Card;
import com.jeffrey.project.card.model.card.Hand;
import com.jeffrey.project.card.model.player.Player;
import com.jeffrey.project.card.model.player.*;

@Component
public class HandChecker {

	@Autowired 
	GameState gameState;
	PlayersList currentPlayers;
	List<Card> flop;
	Card turn;
	Card river;
	String[] namesList; 
		
	public String[] determineWinner() {
		//Get all of the community cards dealt
		List<Card> flop = gameState.getFlop(); 
		Card turn = gameState.getTurn();
		Card river = gameState.getRiver(); 
		
		PlayersList currentPlayers = gameState.getPlayersList(); // returns the current gameState's List of Players
		
		//This will be used to put the players in order of best to worst hands namesList [0] will be the winner
		String[] namesList = new String[currentPlayers.getPlayerCount()]; 
		handChecker(); 
		return namesList; 
	}
	
	public String handChecker () {
		Player currPlayer = currentPlayers.getMaster(); 
		for(int i = 0; i < currentPlayers.getPlayerCount(); i++) {
			namesList[i] = currPlayer.getName(); 
			currPlayer = currPlayer.getNext(); 
		}
		
		
		
		
		
		return "HI" ;
	}
	
}
