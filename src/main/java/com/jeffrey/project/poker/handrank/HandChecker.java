/*
 * AUTHOR: John Zorc
 * Date: May 18 2020
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

	//@Autowired 
	//GameState gameState;
	
	public HandChecker() {}
			
	//Returns a list of players in the order that they are ranked
	public ArrayList<ArrayList<Player>> determineWinner(List<Player> allPlayers, List<Card> flop, Card turn, Card river) {
		
		// We want this to be modularized so we can pass in any list of players (don't want to hardcode current gamestate)
		
		// List<Player> allPlayers = gameState.getAllPlayers();//Player at index 0 should be Master
		ArrayList<ArrayList<Player>> rankingsList = new ArrayList<ArrayList<Player>>();
		int numPlayers = allPlayers.size();
		int[] handRankings = new int[numPlayers];
		int currBestHandVal; //Used to make list of lists
		int numOfTiedHands = 0;
		int currList = 0; 
		int currHandIndex = 0; //Use to keep track of insertions into list of list
		Player currPlayer;
		
		//Initiate lists of lists
		for(int i = 0; i < numPlayers; i++) {
			rankingsList.add(new ArrayList<Player>());
		}
		
		//Give each player their hand rank where the i'th index corresponds to the i'th index in allPlayers list
		for(int i = 0; i < numPlayers; i++) {
			currPlayer = allPlayers.get(i); 
			handRankings[i] = assignHandStrength(currPlayer.getHand(), flop, turn, river);
		}
 
		//Order the players by types of hands best to worst(Ties not checked here)
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
		
		//Check for duplicate values and determine if tie, add to the rankingsList
		currBestHandVal = handRankings[0];
		for(int i = 0; i < numPlayers; i++) {
			if(handRankings[i] == currBestHandVal) {
				numOfTiedHands++;
			}
			else {
				//No tie, just insert the one player into their own list
				if(numOfTiedHands == 1) {
					rankingsList.get(currList).set(0, allPlayers.get(currHandIndex));
					currHandIndex++;
					numOfTiedHands = 0;
					currList++;
					currBestHandVal = handRankings[i];
				}
				//There is a tie, rank the ties then insert based in the rank of the ties
				else {
					ArrayList<Hand> tiedHands = new ArrayList<Hand>();
					int handsInserted = 0;
					int[] handValues = new int[numOfTiedHands];
					
					//Add all the hands of the tied hand type to an array
					for(int j = 0; j < numOfTiedHands; j++) {
						tiedHands.add(allPlayers.get(currHandIndex + j).getCurrentHand());
					}
					
					//Return values starting at 1 that increase when the hand gets worse
					//So all hands with value == 1 are tied as best hands of that hand type
					handValues = tieChecker(tiedHands, currBestHandVal, flop, turn, river);
					int currTieVal = 1;
					int playersInsertedInList = 0;
					while(handsInserted < numOfTiedHands) {
						//Insert all hands of tied value into a list then ++ list, and the currTieVal
						for(int j = 0; j < numOfTiedHands; j++) {
							if(handValues[j] == currTieVal) {
								rankingsList.get(currList).set(playersInsertedInList, allPlayers.get(currHandIndex + j));
								playersInsertedInList++;
								handsInserted++;
							}	
						}
						playersInsertedInList = 0;
						currList++;
						currTieVal++;
					}
					currHandIndex = currHandIndex + numOfTiedHands;
					numOfTiedHands = 0;
					currBestHandVal = handRankings[i];
				}
			}
		}
		return rankingsList; 
	}
	
	
	/* This function will return an int based on how good the hand was 
	 * 1: Royal Flush, 2: Strait Flush, 3: Quads, 4: Full House, 5: Flush, 6: Stright, 7: Trips
	 * 8: Two Pair, 9: Pair, 10: High card
	 */
	public int assignHandStrength(Hand currHand, List<Card> flop, Card turn, Card river) {
		//Get all of the community cards dealt
		ArrayList<Card> playableCards = new ArrayList<Card>();
		playableCards.add(currHand.getCardA());
		playableCards.add(currHand.getCardB());
		playableCards.add(turn);
		playableCards.add(river);
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
	
	//This function will take in an array of hands and a tie specifier number  
	//RETURN ARRAY NUMBERS ASSOCIATED WITH TIED HAND RANK WHERE 1 IS BEST THEN 2...
	public int[] tieChecker(ArrayList<Hand> tiedHands, int tieNumber, List<Card> flop, Card turn, Card river) {
		int numOfTiedHands = tiedHands.size();
		ArrayList<Card> communityCards = new ArrayList<Card>();
		int[] handValues = new int[numOfTiedHands];
		
		communityCards.add(river);
		communityCards.add(turn);
		communityCards.add(flop.get(0));
		communityCards.add(flop.get(1));
		communityCards.add(flop.get(2));
		
		if(tieNumber == 1) {
			for(int i = 0; i < numOfTiedHands; i++) {
				handValues[i] = 1;
			}
		}
		else if(tieNumber == 2) {
			handValues = straitFlushTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 3) {
			handValues = quadsTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 4) {
			handValues = fullHouseTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 5) {
			handValues = flushTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 6) {
			handValues = straitTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 7) {
			handValues = tripsTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 8) {
			handValues = twoPairTieRanker(tiedHands, communityCards);
		}
		else if(tieNumber == 9) {
			handValues = pairTieRanker(tiedHands, communityCards);
		}
		return handValues;
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
		
		for(int i = 0; i < playableCards.size(); i++) {
			currNumber = playableCards.get(i).getNumber();
			for(int j = i + 1; j < playableCards.size(); j ++){
				if(playableCards.get(j).getNumber() == currNumber) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int[] straitFlushTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] quadsTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] fullHouseTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] flushTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] straitTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] tripsTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] twoPairTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int[] tiedHandRankings = new int[tiedHands.size()];
		return tiedHandRankings;
	}
	public int[] pairTieRanker(ArrayList<Hand> tiedHands, ArrayList<Card> communityCards) {
		int numOfTiedHands = tiedHands.size();
		int[] tiedHandRankings = new int[numOfTiedHands];
		int[] highPair = new int[numOfTiedHands];
		int currNum;
		int lastHighestPair = 15; 
		int inserted = 0;
		int currHandRanking = 1;
		
		for(int i = 0; i < numOfTiedHands; i++) {
			int currHighPair = 0;
			ArrayList<Card> currPlayableCards = new ArrayList<Card>();
			currPlayableCards.add(tiedHands.get(i).getCardA());
			currPlayableCards.add(tiedHands.get(i).getCardB());
			currPlayableCards.add(communityCards.get(0));
			currPlayableCards.add(communityCards.get(1));
			currPlayableCards.add(communityCards.get(2));
			currPlayableCards.add(communityCards.get(3));
			currPlayableCards.add(communityCards.get(4));
			
			for(int j = 0; j < currPlayableCards.size(); j++) {
				currNum = currPlayableCards.get(j).getNumber();
				for(int k = j + 1; k < currPlayableCards.size(); k++) {
					if(currPlayableCards.get(k).getNumber() == currNum && currNum >= currHighPair)
						currHighPair = currNum;
				}
			}
			highPair[i] = currHighPair;
		}
		while(inserted < numOfTiedHands) {
			int tempHigh = 0;
			for(int i = 0; i < numOfTiedHands; i++) {
				if(highPair[i] >= tempHigh && highPair[i] < lastHighestPair) {
					tempHigh = highPair[i];
				}
			}
			for(int i = 0; i < numOfTiedHands; i++) {
				if(highPair[i] == tempHigh) {
					tiedHandRankings[i] = currHandRanking;
					inserted++;
				}
			}
			currHandRanking++;
			lastHighestPair = tempHigh; 
		}
		
		return tiedHandRankings;
	}
}





























