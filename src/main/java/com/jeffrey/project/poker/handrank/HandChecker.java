/*
 * AUTHOR: John Zorc
 * Last Edit Date: May 21 2020
 * Email: johnny.zorc@gmail.com
 * 
 * Description: The purpose of this class is to provide the determine winner function
 * that will return a list of lists of all the players. The player/players in the first
 * list will be the winner/winners, the lists are order from best hands to worst
 */
package com.jeffrey.project.poker.handrank;
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
	public static final int ROYAL_FLUSH       = 9000000;
	public static final int STRAIGHT_FLUSH    = 8000000; // + highCard
	public static final int FOUR_OF_A_KIND    = 7000000; // + (Fours Card Rank * 15) + highCard
	public static final int FULL_HOUSE        = 6000000; // + (3's Value * 15) + 2's Value
	public static final int FLUSH             = 5000000; // + highCard
	public static final int STRAIGHT          = 4000000; // + highCard
	public static final int THREE_OF_A_KIND   = 3000000; // + (3's value * 211) + ( High1 * 15 ) + High2
	public static final int TWO_PAIR          = 2000000; // + (High * 211) + (Low * 15) + card
	public static final int ONE_PAIR          = 1000000; // + (pair * 2955) + high1 * 211 + high2 * 15 + high3

	@Autowired 
	GameState gameState;
			
	//Returns a list of players in the order that they are ranked
	public ArrayList<ArrayList<Player>> determineWinner(List<Player> allPlayers, List<Card> flop, Card turn, Card river) {
		//List<Player> allPlayers = gameState.getAllPlayers();//Player at index 0 should be Master
		ArrayList<ArrayList<Player>> rankingsList = new ArrayList<ArrayList<Player>>();
		ArrayList<Card> communityCards = new ArrayList<Card>();
		int numPlayers = allPlayers.size();
		int[] handRankings = new int[numPlayers];
		int[] finalRankings = new int[numPlayers];
		Player currPlayer;
		int currList = 0;
		int lastHighVal = 10000000;//A higher value than possible so that the next high will be less than it on the first iteration
		int currHighVal = 0;
		int inserted = 0;
		
		communityCards.add(flop.get(0));
		communityCards.add(flop.get(1));
		communityCards.add(flop.get(2));
		communityCards.add(turn);
		communityCards.add(river);
		
		
		//Initiate lists of lists
		for(int i = 0; i < numPlayers; i++) {
			rankingsList.add(new ArrayList<Player>());
		}
		
		//ASSIGN EACH PLAYER A TYPE OF HAND
		for(int i = 0; i < numPlayers; i++) {
			currPlayer = allPlayers.get(i); 
			handRankings[i] = assignHandStrength(currPlayer.getHand(), communityCards);
		}
		
		//ADD VALUES TO GIVEN VALUES FOR EACH HAND TYPE TO BREAK TIES
		for(int i = 0; i < numPlayers; i++) {
			int handType = handRankings[i];
			currPlayer = allPlayers.get(i);
			finalRankings[i] = finalRankCalculator(currPlayer.getHand(), handType, communityCards);
		}
 
		//ASSIGNED EACH PLAYER TO A LIST
		while(inserted < numPlayers) {
			//Get highest remaining value/s
			for(int i = 0; i < numPlayers; i++) {
				if(finalRankings[i] > currHighVal && finalRankings[i] < lastHighVal) {
					currHighVal = finalRankings[i];
				}
			}
			//Insert into currList all the highest remaining value/s
			for(int i = 0; i < numPlayers; i++) {
				if(finalRankings[i] == currHighVal) {
					rankingsList.get(currList).add(allPlayers.get(i));
					inserted++;
				}
			}
			lastHighVal = currHighVal;
			currHighVal = 0;
			currList++;
		}
		return rankingsList; 
	}
	
	
	// 1: Royal Flush, 2: Strait Flush ... 10: High Card
	public int assignHandStrength(Hand currHand, ArrayList<Card> communityCards) {
		//Get all of the community cards dealt 
		ArrayList<Card> playableCards = new ArrayList<Card>();
		playableCards.add(currHand.getCardA());
		playableCards.add(currHand.getCardB());
		for(int i = 0; i < communityCards.size(); i++) {
			playableCards.add(communityCards.get(i));
		}

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
	
	//Add's value to the hand type to break ties
	public int finalRankCalculator(Hand currHand, int handType, ArrayList<Card> communityCards) {
		ArrayList<Card> playableCards = new ArrayList<Card>();
		playableCards.add(currHand.getCardA());
		playableCards.add(currHand.getCardB());
		for(int i = 0; i < communityCards.size(); i++) {
			playableCards.add(communityCards.get(i));
		}
		
		int returnVal = 0;
		
		if(handType == 1)
			returnVal = ROYAL_FLUSH; 
		else if(handType == 2) {
			returnVal = straitFlushRanker(playableCards);
		}
		else if(handType == 3) {
			returnVal = quadsRanker(playableCards);
		}
		else if(handType == 4) {
			returnVal = fullHouseRanker(playableCards);
		}
		else if(handType == 5) {
			returnVal = flushRanker(playableCards);
		}
		else if(handType == 6) {
			returnVal = straightRanker(playableCards);
		}
		else if(handType == 7) {
			returnVal = tripsRanker(playableCards);
		}
		else if(handType == 8) {
			returnVal = twoPairRanker(playableCards);
		}
		else if(handType == 9) {
			returnVal = pairRanker(playableCards);
		}
		else if(handType == 10) {
			returnVal = highCardRanker(playableCards);
		}
		//Just here to make sure that the game doesnt break if there are nullptrs passed to the assignHandStrength function
		else if(handType == 11) {
			returnVal = 0;
		}
		return returnVal;
	}
	
	/* ===== HAND TYPE CHECKER FUNCTIONS ===== */
	
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
		int runSize = 0;
		int currNumber;
		int runSuit;
		boolean running = true;
		
		for (int i = 0; i < playableCards.size(); i++) {
			running = true;
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			runSuit = playableCards.get(i).getSuit();
			while(running) {
				running = false;
				for(int j = 0; j < playableCards.size(); j++) {
					if( (playableCards.get(j).getNumber() == currNumber + 1) && (playableCards.get(j).getSuit() == runSuit)) {
						currNumber = playableCards.get(j).getNumber();
						runSize += 1;
						running = true;
					}
				}
				if(runSize == 4 && currNumber == 5) {
					for(int j = 0; j < playableCards.size(); j++) {
						if(playableCards.get(j).getNumber() == 14 && playableCards.get(j).getSuit() == runSuit)
							return true;
					}
				}
				if(runSize >= 5) {
					return true;
				}
			}
		}
		return false; 
	}

	public boolean isQuads(ArrayList<Card> playableCards) {
		int currNumber;
		int counter = 1;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber(); 
			for(int j = 0; j < playableCards.size(); j ++) {
				if(j == i)
					continue;
				else if(playableCards.get(j).getNumber() == currNumber) {
					counter++; 
				}
			}
			if(counter == 4)
				return true;
			else
				counter = 1;
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
		int count = 1;
		
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
				count = 1;
		}
		
		return false;
	}

	public boolean isStrait(ArrayList<Card> playableCards) {
		int runSize;
		int currNumber;
		boolean running = true;
		
		for (int i = 0; i < playableCards.size(); i++) {
			running = true;
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			while(running) {
				running = false;
				for(int j = 0; j < playableCards.size(); j++) {
					if(playableCards.get(j).getNumber() == (currNumber + 1)) {
						currNumber = playableCards.get(j).getNumber(); 
						runSize++;
						//System.out.println("RUNSIZE:" + runSize);
						running = true;
					}
				}
				if(runSize == 4 && currNumber == 5) {
					for(int j = 0; j < playableCards.size(); j++) {
						if(playableCards.get(j).getNumber() == 14)
							return true;
					}
				}
				if(runSize >= 5) 
					return true;
			}
		}
		return false; 
	}

	public boolean isTrips(ArrayList<Card> playableCards) {
		int currNumber;
		int counter = 1;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber(); 
			for(int j = 0; j < playableCards.size(); j ++) {
				if (i == j)
					continue;
				else if(playableCards.get(j).getNumber() == currNumber) {
					counter++; 
				}
			}
			if(counter == 3)
				return true;
			else
				counter = 1;
		}
		return false;
	}
	
	public boolean isTwoPair(ArrayList<Card> playableCards) {
		int numForPairOne = 0;
		boolean foundOnePair = false; 
		
		for(int i = 0; i < playableCards.size(); i++) {
			for(int j = i + 1; j < playableCards.size(); j++){
				if(playableCards.get(j).getNumber() == playableCards.get(i).getNumber()) {
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
		int counter = 1;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber();
			for(int j = 0; j < playableCards.size(); j ++){
				if(i == j)
					continue;
				else if(playableCards.get(j).getNumber() == currNumber) {
					counter++;
				}
			}
			if(counter == 2)
				return true;
			else
				counter = 1;
		}
		return false;
	}
	

	
	/* ===== RANKER FUNCTIONS FOR EACH HAND TYPE ===== */
	
	public int straitFlushRanker(ArrayList<Card> playableCards) {
		int returnVal = STRAIGHT_FLUSH;
		int highCard = 0;
		int runSize = 0;
		int currNumber;
		int runSuit;
		boolean running = true;
		
		for (int i = 0; i < playableCards.size(); i++) {
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			runSuit = playableCards.get(i).getSuit();
			while(running) {
				running = false;
				for(int j = 0; j < playableCards.size(); j++) {
					if( (playableCards.get(j).getNumber() == currNumber + 1) && (playableCards.get(j).getSuit() == runSuit)) {
						currNumber = playableCards.get(j).getNumber();
						runSize += 1;
						running = true;
						if(runSize >= 5)
							highCard = playableCards.get(j).getNumber();
					}
				}
			}
		}
		
		
		
		returnVal = returnVal + highCard;
		return returnVal;
	}
	
	public int quadsRanker(ArrayList<Card> playableCards) {
		int returnVal = FOUR_OF_A_KIND;
		int quadsVal = 0;
		int highCard = 0;
		int counter = 0;
		int currVal;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currVal = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j++) {
				if(currVal == playableCards.get(j).getNumber())
					counter++;
			}
			if(counter == 4) {
				quadsVal = playableCards.get(i).getNumber();
				break;
			}
			else {
				counter = 0;
			}
		}
		for(int i = 0; i < playableCards.size(); i++) {
			if(playableCards.get(i).getNumber() != quadsVal && playableCards.get(i).getNumber() > highCard)
				highCard = playableCards.get(i).getNumber();
		}
		
		returnVal = returnVal + (quadsVal * 15) + highCard;
		return returnVal;
	}
	
	public int fullHouseRanker(ArrayList<Card> playableCards) {
		int returnVal = FULL_HOUSE;
		int tripsVal = 0;
		int pairVal = 0;
		int counter = 0;
		int currVal;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currVal = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j++) {
				if(currVal == playableCards.get(j).getNumber())
					counter++;
			}
			if(counter == 2) {
				pairVal = playableCards.get(i).getNumber();
			}
			else if (counter == 3) {
				tripsVal = playableCards.get(i).getNumber();
			}
			counter = 0;		
		}
		
		returnVal = returnVal + (tripsVal * 15) + pairVal;
		
		return returnVal;
	}
	
	public int flushRanker(ArrayList<Card> playableCards) {
		int returnVal = FLUSH;
		int count = 1;
		int highCard = 0;
		
		for (int i = 0; i < playableCards.size(); i++) {
			int currSuit = playableCards.get(i).getSuit();
			for(int j = i + 1; j < playableCards.size(); j++) {
				if(playableCards.get(j).getSuit() == currSuit) {
					count++;
					if(playableCards.get(j).getNumber() > highCard)
						highCard = playableCards.get(j).getNumber();
				}
			}
			if(count >= 5) {
				break;
			}
			else 
				count = 1;
				highCard = 0;
		}
		returnVal = returnVal + highCard;
		
		return returnVal;
	}
	
	public int straightRanker(ArrayList<Card> playableCards) {
		int returnVal = STRAIGHT;
		int runSize = 0;
		int currNumber;
		int highCard = 0;
		boolean foundRun = false;
		
		//look for a run of length 5
		for (int i = 0; i < playableCards.size(); i++) {
			runSize = 1;
			currNumber = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j++) {
				if(playableCards.get(j).getNumber() == currNumber + 1) {
					currNumber = playableCards.get(j).getNumber();
					runSize += 1;
				}
				else {
					runSize = 1;
				}
				if(runSize == 5) {
					highCard = playableCards.get(j).getNumber();
					foundRun = true;
					break;
				}
			}
			if(foundRun)
				break;
		}
		returnVal = returnVal + highCard;
		
		return returnVal;
	}
	
	public int tripsRanker(ArrayList<Card> playableCards) {
		int returnVal = THREE_OF_A_KIND;
		int tripsVal = 0;
		int high1 = 0;
		int high2 = 0;
		int currCard;
		int counter = 0;
		
		for(int i = 0; i < playableCards.size(); i++) {
			currCard = playableCards.get(i).getNumber();
			for (int j = i + 1; j < playableCards.size(); j++) {
				if(playableCards.get(j).getNumber() == currCard)
					counter++;
			}
			if(counter == 3) {
				tripsVal = playableCards.get(i).getNumber();
				break;
			}
			counter = 0;
		}
		for(int i = 0; i < playableCards.size(); i++) {
			if (playableCards.get(i).getNumber() != tripsVal && playableCards.get(i).getNumber() > high1)
				high1 = playableCards.get(i).getNumber();
		}
		for(int i = 0; i < playableCards.size(); i++) {
			if (playableCards.get(i).getNumber() != tripsVal && playableCards.get(i).getNumber() != high1
				&& playableCards.get(i).getNumber() > high2)
				high2 = playableCards.get(i).getNumber();
		}
		returnVal = returnVal + (tripsVal * 211) + (high1 * 15) + high2;
		
		return returnVal;
	}
	
	public int twoPairRanker(ArrayList<Card> playableCards) {
		int returnVal = TWO_PAIR;
		int pairOneVal = 0;
		int pairTwoVal = 0;
		int highCard = 0;
		boolean foundOnePair = false;
		
		for(int i = 0; i < playableCards.size(); i++) {
			for(int j = i + 1; j < playableCards.size(); j++){
				if(playableCards.get(j).getNumber() == playableCards.get(i).getNumber()) {
					if(foundOnePair == false) {
						pairOneVal = playableCards.get(j).getNumber();
						foundOnePair = true;
					}
					else {
						if(playableCards.get(j).getNumber() != pairOneVal) {
							pairTwoVal = playableCards.get(i).getNumber();
						}
					}
				}
			}
		}
		
		
		for(int i = 0; i < playableCards.size(); i++) {
			if(playableCards.get(i).getNumber() != pairOneVal && playableCards.get(i).getNumber() != pairTwoVal 
				&& playableCards.get(i).getNumber() > highCard)
				highCard = playableCards.get(i).getNumber();
		}
		returnVal = returnVal + (pairOneVal * 211) + (pairTwoVal * 15) + (highCard);
		
		return returnVal;
	}
	
	public int pairRanker(ArrayList<Card> playableCards) {
		int returnVal = ONE_PAIR;
		int pairVal = 0;
		int high1 = 0;
		int high2 = 0;
		int high3 = 0;
		
		for(int i = 0; i < playableCards.size(); i++) {
			for(int j = i + 1; j < playableCards.size(); j ++){
				if(playableCards.get(j).getNumber() == playableCards.get(i).getNumber()) {
					pairVal = playableCards.get(i).getNumber();
					break;
				}
			}
		}
		
		for(int i = 0; i < playableCards.size(); i++) {
			if(playableCards.get(i).getNumber() != pairVal && playableCards.get(i).getNumber() > high1)
				high1 = playableCards.get(i).getNumber();
		}
		for(int i = 0; i < playableCards.size(); i++) {
			if(playableCards.get(i).getNumber() != pairVal && playableCards.get(i).getNumber() != high1 
				&& playableCards.get(i).getNumber() > high2)
				high2 = playableCards.get(i).getNumber();
		}
		for(int i = 0; i < playableCards.size(); i++) {
			if(playableCards.get(i).getNumber() != pairVal && playableCards.get(i).getNumber() != high1 
				&& playableCards.get(i).getNumber() != high2 && playableCards.get(i).getNumber() > high3)
					high3 = playableCards.get(i).getNumber();
		}
		
		returnVal = returnVal + (pairVal * 2955) + (high1 * 211) + (high2 * 15) + (high3);
		
		return returnVal;
	}
	
	public int highCardRanker(ArrayList<Card> playableCards) {
		int returnVal = 0;
		int high1 = 0;
		int high2 = 0;
		int high3 = 0;
		int high4 = 0;
		int high5 = 0;
		int currNum; 
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNum = playableCards.get(i).getNumber();
			if(currNum > high1)
				high1 = currNum;
		}
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNum = playableCards.get(i).getNumber();
			if(currNum != high1 && currNum > high2)
				high2 = currNum;
		}
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNum = playableCards.get(i).getNumber();
			if(currNum != high1 && currNum != high2 && currNum > high3)
				high3 = currNum;
		}
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNum = playableCards.get(i).getNumber();
			if(currNum != high1 && currNum != high2 && currNum != high3 && currNum > high4)
				high4 = currNum;
		}
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNum = playableCards.get(i).getNumber();
			if(currNum != high1 && currNum != high2 && currNum != high3 && currNum != high4 && currNum > high5)
				high5 = currNum;
		}
		returnVal = returnVal + (high1 * 41370) + (high2 * 2955) + (high3 * 211) + (high4 * 15) + high5;
		
		return returnVal;
	}

}
