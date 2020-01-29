package com.jeffrey.project.card.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.card.Card;
import com.jeffrey.project.card.model.card.Deck;
import com.jeffrey.project.card.model.player.Player;
import com.jeffrey.project.card.model.player.PlayersList;

@Component
public class GameState {
	
	PlayersList playersList;
	String runStatus; // preflop, flop, turn, river, unning, paused, notStarted
	Deck deck;
	List<Card> flop;
	Card turn;
	Card river;
	Player currTurn;
	double smallBlind; 
	double bigBlind;
	double pot;
	double mostRecentBetSize; 
	
	public GameState() {
		
//		this.playersList = new PlayersList();
//		this.runStatus = "notStarted";
//		this.deck = new Deck();
		
		
		this.playersList = new PlayersList();
		this.runStatus = "notStarted";
		this.deck = new Deck();
		this.flop = new ArrayList<Card> ();
		this.turn = null;
		this.river = null;
		this.runStatus = "running";
		this.smallBlind = 1; 
		this.bigBlind = 110;
		this.pot = 0;
		addPlayer("Jeffrey",  100); 
		addPlayer("Johnny",  100);
		addPlayer("Mark",  100);
		addPlayer("Jared", 105);
	}
	
	public int addPlayer(String name, double chipCount) {
		if(playersList.getPlayerByName(name) == null) {
			playersList.addPlayer(name, chipCount);
			return 1;
		} else {
			return -1;
		}
	}
	
	public int removePlayer(String name) {
		if(playersList.getPlayerByName(name) != null) {
			playersList.removePlayer(name);
			return 1;
		} else {
			return -1;
		}
	}
	
	public void fold(Player player) {
		if(currTurn == player) {
			player.setToFolded();
		}
	}
	
	public List<Player> getAllPlayers() {
		List<Player> players = new ArrayList<Player>();
		if(playersList.getPlayerCount() != 0) {
			Player currPlayer = playersList.getMaster();
			do {
				players.add(currPlayer);
				currPlayer = currPlayer.next;
			} while(currPlayer != playersList.getMaster());
		} 
		return players;	
	}
	
	public void startHand() {
		
		playersList.setAllToWaiting();
		
		runStatus = "preflop";
		
		dealCards(); 
		
		// set the small blind: 
		Player smallBlindPlayer = playersList.getDealer().next;
		if(smallBlindPlayer.getChipCount() < smallBlind) {
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
		
		//set the big blind:
		Player bigBlindPlayer = playersList.getDealer().next.next;
		if(bigBlindPlayer.getChipCount() < bigBlind) {
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
		bigBlindPlayer.setToBet();
				
		//set the action: 
		Player firstActionPlayer = playersList.getDealer().next.next.next; 
		firstActionPlayer.setToAction(); 
		currTurn = firstActionPlayer;
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

	@Override
	public String toString() {
		return "GameState [playersList=" + playersList + ", gameStatus=" + runStatus + "]";
	}
	
	
	
}