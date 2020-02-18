package com.jeffrey.project.card.handrank;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.GameState;
import com.jeffrey.project.card.model.card.Hand;
import com.jeffrey.project.card.model.player.Player;

@Component
public class HandChecker {

	@Autowired 
	GameState gameState;
	
	public List<Integer> determineWinner(List<Hand> hands) {
		// add the index(s) of the winner(s) to this list and return it at the end: 
		List<Integer> winnerList = new ArrayList<Integer>();
		
		/*
		 * 
		 * hand check code goes right here 
		 * 
		 * 
		 */
		
		
		return winnerList; 
	}
	
}
