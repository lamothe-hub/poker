package com.jeffrey.project.poker.model;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jeffrey.project.poker.exceptions.GhostHandException;
import com.jeffrey.project.poker.exceptions.PlayerAlreadyExistsException;
import com.jeffrey.project.poker.exceptions.PotEmptyException;
import com.jeffrey.project.poker.handrank.HandChecker;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Deck;
import com.jeffrey.project.poker.model.player.Player;
import com.jeffrey.project.poker.model.player.PlayersList;
import com.jeffrey.project.poker.rest.StateController;

@Component
public class GameState {
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	PlayersList playersList;
	String runStatus; // preflop, flop, turn, river, running, paused, notStarted, onlyOnePlayer
	Deck deck;
	List<Card> flop;
	Card turn;
	Card river;
	Player currTurn;
	double maxBuyIn;
	double smallBlind;
	double bigBlind;
	double pot;
	double mostRecentBetSize;
	Player mostRecentActionReset;
	public GameState() {

//		this.playersList = new PlayersList();
//		this.runStatus = "notStarted";
//		this.deck = new Deck();

		this.playersList = new PlayersList();
		this.runStatus = "notStarted";
		this.deck = new Deck();
		this.flop = new ArrayList<Card>();
		this.turn = null;
		this.river = null;
		this.runStatus = "running";
		this.smallBlind = 1;
		this.bigBlind = 2;
		this.pot = 0;

		
	}
	
	public void reset() {
		this.playersList = new PlayersList();
		this.runStatus = "notStarted";
		this.deck = new Deck();
		this.flop = new ArrayList<Card>();
		this.turn = null;
		this.river = null;
		this.runStatus = "running";
		this.smallBlind = 1;
		this.bigBlind = 2;
		this.pot = 0;
	}

	public void addPlayer(String name, double chipCount) throws PlayerAlreadyExistsException {
		if (playersList.getPlayerByName(name) == null) {
			playersList.addPlayer(name, chipCount);
		} else {
			throw new PlayerAlreadyExistsException(name);
		}
	}

	public int removePlayer(String name) {
		if (playersList.getPlayerByName(name) != null) {
			playersList.removePlayer(name);
			return 1;
		} else {
			return -1;
		}
	}

	public void fold(Player player) {
		if (currTurn == player) {
			player.setToFolded();
		}
	}

	public List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();
		if (playersList.getPlayerCount() != 0) {
			Player currPlayer = playersList.getMaster();
			do {
				players.add(currPlayer);
				currPlayer = currPlayer.next;
			} while (currPlayer != playersList.getMaster());
		}
		return players;
	}
	
	public void distributeMoneyOneRemainingActive() {
		if(playersList.getPlayersInPlay().size() == 1) {
			Player player = playersList.getPlayersInPlay().get(0);
			try {
				payFromPot(player, pot);
			} catch(Exception ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	public void distributeMoneyToWinners() throws PotEmptyException {
		
		HandChecker handChecker = new HandChecker();

		ArrayList<ArrayList<Player>> rankingsList = handChecker.determineWinner(playersList.getPlayersInPlay(), flop, turn, river); 
		int i = 1;
		System.out.println("\n");
		if(rankingsList.size() > 0) {
			
			for(ArrayList<Player> tier: rankingsList) {
				
				if(pot > 0) {
					
					// Handle the all in people - chop the pot 
					boolean removedAnAllIn = true;
					while(removedAnAllIn) {
						removedAnAllIn = false;
						
						int numPlayersInTier = tier.size();
						double maxCanTake = pot / numPlayersInTier;

						// Payout the people who are all in and can only receive a slice of pot
						
						try {
							Iterator<Player> itr = tier.iterator();
							while(itr.hasNext()) {
								Player player = itr.next();
								if(player.isAllIn() && player.getMaxCanEarnThisRound() < maxCanTake ){
									
									payFromPot(player, player.getMaxCanEarnThisRound());

									tier.remove(player);
									removedAnAllIn = true;
								} 
							}	
						} catch(ConcurrentModificationException ex) {
							System.err.println("First block");
							System.err.println(ex.getMessage());
						}
						
						
						
					}
					
					// Now that the all in people are handled, disburse rest of the money for the tier
					if(!tier.isEmpty()) {
						int numPlayersInTier = tier.size();
						double maxCanTake = pot / numPlayersInTier;
						
						try {
							Iterator<Player> itr = tier.iterator();
							while(itr.hasNext()) {
								Player player = itr.next();
								payFromPot(player, maxCanTake);
								//tier.remove(player);
							}
						} catch(ConcurrentModificationException ex) {
							System.err.println("Second block"); 
							System.err.println(ex.getMessage());
						}
						
					}
					
				}
				i++;
			}
		}
		
		
		
		
	}
	
	public void payFromPot(Player player, double amount) throws PotEmptyException {
		if(amount > pot + .001) {
			throw new PotEmptyException(player, amount, pot);
		}
		pot = pot - amount; 
		player.addChips(amount);
	}

	public void startHand() throws GhostHandException {

		playersList.setAllToWaiting();

		runStatus = "preFlop";

		wipeChips();
		dealCards();

		// set the small blind:
		Player smallBlindPlayer = playersList.getDealer().getNextInPlay();
		if (smallBlindPlayer.getChipCount() < smallBlind) {
			// take whatever they have and add it to the pot
			double availableAmount = smallBlindPlayer.getChipCount();
			smallBlindPlayer.subtractChips(availableAmount);
			smallBlindPlayer.setCurrAmountInPot(availableAmount);
			smallBlindPlayer.setCurrAmountThisRound(availableAmount);
			addToPot(availableAmount);
		} else {
			smallBlindPlayer.subtractChips(smallBlind);
			smallBlindPlayer.setCurrAmountInPot(smallBlind);
			smallBlindPlayer.setCurrAmountThisRound(smallBlind);
			addToPot(smallBlind);
		}
		smallBlindPlayer.setToBet();

		// set the big blind:
		Player bigBlindPlayer = smallBlindPlayer.getNextInPlay();
		if (bigBlindPlayer.getChipCount() < bigBlind) {
			// take whatever they have and add it to the pot
			double availableAmount = bigBlindPlayer.getChipCount();
			bigBlindPlayer.subtractChips(availableAmount);
			bigBlindPlayer.setCurrAmountInPot(availableAmount);
			bigBlindPlayer.setCurrAmountThisRound(availableAmount);
			addToPot(availableAmount);
		} else {
			bigBlindPlayer.subtractChips(bigBlind);
			bigBlindPlayer.setCurrAmountInPot(bigBlind);
			bigBlindPlayer.setCurrAmountThisRound(bigBlind);
			addToPot(bigBlind);
		}
		this.mostRecentBetSize = this.bigBlind;
		bigBlindPlayer.setToBB();

		// set the action:
		Player firstActionPlayer = bigBlindPlayer.getNextInPlay();
		firstActionPlayer.setToAction();
		currTurn = firstActionPlayer;
		mostRecentActionReset = firstActionPlayer;
	}

	public void dealCards() {

		deck.shuffle();
		flop = new ArrayList<Card>();
		this.turn = null;
		this.river = null;

		Player currPlayer = playersList.getDealer();

		do {
			currPlayer.next.getHand().setCardA(deck.nextCard());
			currPlayer = currPlayer.next;
		} while (currPlayer != playersList.getDealer());

		do {
			currPlayer.next.getHand().setCardB(deck.nextCard());
			currPlayer = currPlayer.next;
		} while (currPlayer != playersList.getDealer());
	}

	public void dealFlop() {
		deck.nextCard(); // burn card
		flop.add(deck.nextCard());
		flop.add(deck.nextCard());
		flop.add(deck.nextCard());
	}
	
	public void wipeChips() {
		this.pot = 0; 
		this.mostRecentBetSize = 0; 
		Player currPlayer = playersList.getMaster(); 
		do {
			currPlayer.wipeChips();
			currPlayer = currPlayer.getNext();
		} while(currPlayer != playersList.getMaster());
	}

	public void dealTurn() {
		deck.nextCard(); // burn card
		turn = deck.nextCard();
	}

	public void dealRiver() {
		deck.nextCard(); // burn card
		river = deck.nextCard();
	}

	public List<Card> shuffle() {
		return deck.shuffle();
	}

	public Deck getDeck() {
		return deck;
	}

	public String getGameStatus() {
		return runStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.runStatus = gameStatus;
	}

	public PlayersList getPlayersList() {
		return playersList;
	}

	public void setPlayersList(PlayersList playersList) {
		this.playersList = playersList;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public double getMostRecentBetSize() {
		return mostRecentBetSize;
	}

	public void setMostRecentBetSize(double mostRecentBetSize) {
		this.mostRecentBetSize = mostRecentBetSize;
	}

	public List<Card> getFlop() {
		return flop;
	}

	public void setFlop(List<Card> flop) {
		this.flop = flop;
	}

	public Card getTurn() {
		return turn;
	}

	public void setTurn(Card turn) {
		this.turn = turn;
	}

	public Card getRiver() {
		return river;
	}

	public void setRiver(Card river) {
		this.river = river;
	}

	public void setDeck(Deck deck) {
		this.deck = deck;
	}

	public double getPot() {
		return pot;
	}

	public void addToPot(double amount) {
		pot += amount;
	}

	public Player getCurrTurn() {
		return this.currTurn;
	}

	public double getSmallBlind() {
		return smallBlind;
	}

	public void setSmallBlind(double smallBlind) {
		this.smallBlind = smallBlind;
	}

	public double getBigBlind() {
		return bigBlind;
	}

	public void setBigBlind(double bigBlind) {
		this.bigBlind = bigBlind;
	}

	public void setCurrTurn(Player currTurn) {
		this.currTurn = currTurn;
	}

	public void setPot(double pot) {
		this.pot = pot;
	}

	public Player getMostRecentActionReset() {
		return mostRecentActionReset;
	}

	public double getMaxBuyIn() {
		return maxBuyIn;
	}

	public void setMaxBuyIn(double maxBuyIn) {
		this.maxBuyIn = maxBuyIn;
	}

	public void setMostRecentActionReset(Player mostRecentBettor) {
		this.mostRecentActionReset = mostRecentBettor;
	}
	
	public void setOnlyOnePlayer() {
		this.runStatus = "onlyOnePlayer";
	}
	
	public boolean onlyOnePlayer() {
		if(runStatus.equals("onlyOnePlayer")) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "GameState [playersList=" + playersList + ", gameStatus=" + runStatus + "]";
	}

}
