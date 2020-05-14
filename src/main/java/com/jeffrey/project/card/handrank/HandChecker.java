package com.jeffrey.project.card.handrank;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;
import com.jeffrey.project.poker.model.player.Player;

@Component
public class HandChecker {

	@Autowired 
	GameState gameState;
			
	//Returns a list of players in the order that they are ranked
	public List<Player> determineWinner() {
		
		List<Player> allPlayers = gameState.getAllPlayers();//Player at index 0 should be Master
		int numPlayers = allPlayers.size();
		int[] handRankings = new int[numPlayers];
		
		Player currPlayer;
		
		//Give each player their hand rank where the i'th index corresponds to the ith index in allplayers list
		for(int i = 0; i < numPlayers; i++) {
			currPlayer = allPlayers.get(i); 
			handRankings[i] = handChecker(currPlayer.getHand());
		}
 
		for(int i = 1; i < numPlayers; i++) {
			for(int j = i; j > 0 ; j--) {
				if(handRankings[j] < handRankings[j - 1]) {
					int temp = handRankings[j];
					Player tempPlayerHolder = allPlayers.get(j);
					allPlayers.set(j, allPlayers.get(j-1));
					allPlayers.set(j-1, tempPlayerHolder);
					handRankings[j] = handRankings[j -1];
					handRankings[j - 1] = temp;
				}
			}
		}
		
		return allPlayers; 
	}
	
	
	/* This function will return an int based on how good the hand was 
	 * 1: Royal Flush, 2: Strait Flush, 3: Quads, 4: Full House, 5: Flush, 6: Trips, 7: Two Pair
	 * 8: Pair, 9: High Card
	 */
	public int handChecker(Hand currHand) {
		//Get all of the community cards dealt
		List<Card> flop = gameState.getFlop(); 
		ArrayList<Card> playableCards = new ArrayList<Card>();
		playableCards.add(currHand.getCardA());
		playableCards.add(currHand.getCardB());
		playableCards.add(gameState.getTurn());
		playableCards.add(gameState.getRiver());
		playableCards.add(flop.get(0));
		playableCards.add(flop.get(1));
		playableCards.add(flop.get(2));
		 
		if(isRoyalFlush(playableCards)) {
			return 1;
		}
		else if(isStraitFlush(playableCards)) {
			return 2;
		}
		else if(isQuads(playableCards)) {
			return 3;
		}
		else if(isFullHouse(playableCards)) {
			return 4;
		}
		else if (isFlush(playableCards)) {
			return 5;
		}
		else if(isStrait(playableCards)) {
			return 6;
		}
		else if(isTrips(playableCards)){
			return 7;
		}
		else if(isTwoPair(playableCards)) {
			return 8;
		}
		else if(isPair(playableCards)) {
			return 9;
		}
		else
			return 10;
	}
	
	public boolean isRoyalFlush(ArrayList<Card> playableCards) {
		boolean running = true; 
		int runSize = 0;
		int currNumber;
		int runSuit; //Check this on every number that is currNumber + 1
		
		//look for a run of length 5
		for (int i = 0; i < playableCards.size(); i++) {
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			runSuit = playableCards.get(i).getSuit();
			
			//Only continue to loop if you found a suitable(haha get it) next card in the prev iteration
			while(running) {
				running = false;  
				if(runSize == 5 && currNumber == 14)//14 is ace
					return true;
				for(int j = 0; j < playableCards.size(); j++) {
					//Is next card 1 greater than currCard and also the same suit 
					if( (playableCards.get(j).getNumber() == currNumber + 1) && (playableCards.get(j).getSuit() == runSuit)) {
						currNumber = playableCards.get(j).getNumber();
						running = true; 
						runSize += 1;
						break;
					}
				}
			}
		}
		return false;
	}
	public boolean isStraitFlush(ArrayList<Card> playableCards) {
		boolean running = true; 
		int runSize = 0;
		int currNumber;
		int runSuit; //Check this on every number that is currNumber + 1
		
		//look for a run of length 5
		for (int i = 0; i < playableCards.size(); i++) {
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			runSuit = playableCards.get(i).getSuit();
			
			while(running) {
				running = false; 
				if(runSize == 5)
					return true;
				for(int j = 0; j < playableCards.size(); j++) {
					//Is next card 1 greater than currCard and also the same suit 
					if( (playableCards.get(j).getNumber() == currNumber + 1) && (playableCards.get(j).getSuit() == runSuit)) {
						currNumber = playableCards.get(j).getNumber();
						running = true; 
						runSize += 1;
						break;
					}
				}
			}
		}
		return false; 
	}
	public boolean isQuads(ArrayList<Card> playableCards) {
		int currNumber;
		int counter = 0;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber(); 
			for(int j = i + 1; j < playableCards.size(); j ++) {
				if(playableCards.get(j).getNumber() == currNumber) {
					counter++; 
				}
			}
			if(counter == 4)
				return true;
			else
				counter = 0;
		}
		return false;
	}
	public boolean isFullHouse(ArrayList<Card> playableCards) {
		if( (isPair(playableCards)) && (isTrips(playableCards)) )
			return true;
		else 
			return false;
	}
	public boolean isFlush(ArrayList<Card> playableCards) {
		int count = 0;
		
		for (int i = 0; i < playableCards.size(); i++) {
			int currSuit = playableCards.get(i).getSuit();
			for(int j = i + 1; j < playableCards.size(); j++) {
				if(playableCards.get(j).getSuit() == currSuit) {
					count++;
				}
			}
			if(count >= 5) {
				return true;
			}
			else 
				count = 0;
		}
		
		return false;
	}
	public boolean isStrait(ArrayList<Card> playableCards) {
		boolean running = true; //used to see check for runs of 5
		int runSize = 0;
		int currNumber;
		
		//look for a run of length 5
		for (int i = 0; i < playableCards.size(); i++) {
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			
			while(running) {
				running = false; 
				if(runSize == 5)
					return true;
				for(int j = 0; j < playableCards.size(); j++) {
					if(playableCards.get(j).getNumber() == currNumber + 1) {
						currNumber = playableCards.get(j).getNumber();
						running = true; 
						runSize += 1;
						break;
					}
				}
			}
		}
		return false; 
	}
	public boolean isTrips(ArrayList<Card> playableCards) {
		int currNumber;
		int counter = 0;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber(); 
			for(int j = i + 1; j < playableCards.size(); j ++) {
				if(playableCards.get(j).getNumber() == currNumber) {
					counter++; 
				}
			}
			if(counter == 3)
				return true;
			else
				counter = 0;
		}
		return false;
	}
	public boolean isTwoPair(ArrayList<Card> playableCards) {
		int currNumber;
		int numForPairOne = 0;
		boolean foundOnePair = false; 
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j ++){
				if(playableCards.get(j).getNumber() == currNumber) {
					if(foundOnePair == false) {
						numForPairOne = playableCards.get(j).getNumber();
						foundOnePair = true;
					}
					else {
						if(playableCards.get(j).getNumber() != numForPairOne) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public boolean isPair(ArrayList<Card> playableCards) {
		int currNumber;
		int count = 0;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j ++){
				if(playableCards.get(j).getNumber() == currNumber) {
					count++;
				}
			}
			if(count == 2) {
				return true;
			}
			else 
				count = 0;
		}
		return false;
	}
}





























