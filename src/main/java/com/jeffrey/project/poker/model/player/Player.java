package com.jeffrey.project.poker.model.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffrey.project.poker.exceptions.GhostHandException;
import com.jeffrey.project.poker.exceptions.InvalidCallException;
import com.jeffrey.project.poker.exceptions.OnePlayerException;
import com.jeffrey.project.poker.model.card.Hand;
import com.jeffrey.project.poker.rest.StateController;

public class Player {
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	String name;
	double chipCount;
	double currAmountThisRound;
	double currAmountInPot;
	double maxCanEarnThisRound;
	Hand currentHand;
	public Player next;
	String status; 
	int hashCode;

	/*
	 * Status options:
	 * 		-action: it is this players turn - awaiting his choice
	 * 		-waiting: this player is in the hand but is waiting for action
	 * 		-folded: this player has folded the hand
	 * 		-bet: this player has bet and is awaiting player responses
	 * 		-called: this player has already called the bet 
	 * 		-allIn
	 * 		-NA: this is the status of a player upon initialization  
	 */
	
	public Player(String name) {
		this.name = name;
	}
	
	
	
	public Player(String name, double chipCount) {
		this.name = name; 
		this.chipCount = chipCount; 
		this.currAmountThisRound = 0; 
		this.currAmountInPot = 0;
		this.next = null;
		this.status = "NA";
		this.currentHand = new Hand();
	}
	
	public Player(String name, double chipCount, int hashCode) {
		this.name = name; 
		this.chipCount = chipCount; 
		this.currAmountThisRound = 0; 
		this.currAmountInPot = 0;
		this.next = null;
		this.status = "NA";
		this.currentHand = new Hand();
		this.hashCode = hashCode;
		
	}
	
	public double placeBet(double amount) {
		// real amount accounts for chipCount jsut to make sure we didnt miss anything
		double additionalAmount = amount - currAmountThisRound;
		chipCount -= additionalAmount; 
		currAmountThisRound += additionalAmount; 
		currAmountInPot += additionalAmount; 
		setToBet();
		return additionalAmount;
	
	}
	
	public double makeCall(double amount, double mostRecentBetSize) {
		double realAmount;
		
		if(mostRecentBetSize - currAmountThisRound > chipCount) {
			realAmount = chipCount;
		} else {
			realAmount = mostRecentBetSize - currAmountThisRound;
			if(realAmount != amount) {
				throw(new InvalidCallException(name, amount));
			}
		}
		chipCount -= realAmount; 
		currAmountThisRound += realAmount; 
		currAmountInPot += realAmount; 
		setToCalled();
		return realAmount;
		
	}

	public boolean inHand() {
		if(status.equals("action") || status.equals("waiting")
				|| status.equals("bet") || status.equals("called")
				|| status.equals("checked") || status.equals("bigBlind")) {
			return true;
		}
		return false;
	}
	
	public void subtractChips(double chipLoss) {
		chipCount -= chipLoss;
	}
	
	public void addChips(double chipGain) {
		chipCount += chipGain;
	}
	public void setToCalled() {
		status = "called";
	}
	public void setToBet() {
		status = "bet";
	}
	public void setToChecked() {
		status = "checked";
	}
	public void setToFolded() {
		status = "folded";
	}
	public void setToWaiting() {
		status = "waiting";
	} 
	
	public void setToAction() {
		status = "action";
	}
	public void setToBB() {
		status = "bigBlind";
	}
	
	public String getName() {
		return name;
	}

	public String getStatus() {
		return status;
	}

//	public void setStatus(String status) {
//		this.status = status;
//	}

	public double getChipCount() {
		return chipCount;
	}

	public void setChipCount(double chipCount) {
		this.chipCount = chipCount;
	}

	public Hand getCurrentHand() {
		return currentHand;
	}

	public void setCurrentHand(Hand currentHand) {
		this.currentHand = currentHand;
	}

	public Player getNext() {
		return next;
	}
	
	public Player getNextInPlay() {
		// Returns the next player that is still in the hand and has opportunity for action
		
		Player firstNextPlayer = getNext();
		Player currPlayer = getNext();
		
		while(currPlayer.getNext() != firstNextPlayer) {
			if(currPlayer.inHand()) {
				return currPlayer;
			}
			currPlayer = currPlayer.getNext();
		}
		
		return currPlayer;
	
	}
	
	public Player getNextActive() throws OnePlayerException {
		// Returns the next player that is still in the hand and has opportunity for action
		
		Player firstNextPlayer = getNext();
		Player currPlayer = getNext();
		
		while(currPlayer.getNext() != firstNextPlayer) {
			if(currPlayer.isActive()) {
				return currPlayer;
			}
			currPlayer = currPlayer.getNext();
		}
		
		throw new OnePlayerException(currPlayer);
		
	}

	public void wipeChips() {
		this.currAmountThisRound = 0;
		this.currAmountInPot = 0;
	}
	
	public void setNext(Player next) {
		this.next = next;
	}

	public void setName(String name) {
		this.name = name;
	} 
	public double getCurrAmountThisRound() {
		return currAmountThisRound;
	}

	public void setCurrAmountThisRound(double currAmountThisRound) {
		this.currAmountThisRound = currAmountThisRound;
	}

	public double getCurrAmountInPot() {
		return currAmountInPot;
	}
	
	

	public int getHashCode() {
		return hashCode;
	}



	public void setCurrAmountInPot(double currAmountInPot) {
		this.currAmountInPot = currAmountInPot;
	}
	public Hand getHand() {
		return currentHand;
	}
	
	public void setAllIn() {
		this.status = "allIn";
	}

	public double getMaxCanEarnThisRound() {
		return maxCanEarnThisRound;
	}

	public boolean isAllIn() {
		if(status.equals("allIn")) {
			return true; 
		}
		return false;
	}

	public void setMaxCanEarnThisRound(double maxCanEarnThisRound) {
		this.maxCanEarnThisRound = maxCanEarnThisRound;
	}
	
	public boolean isActive() {
		if(status.equals("inactive")) {
			return false;
		}
		return true;
	}
	
	public void setInactive() {
		status = "inactive";
	}



	@Override
	public String toString() {
		return "Player [name=" + name + ", chipCount=" + chipCount + ", currAmountThisRound=" + currAmountThisRound
				+ ", currAmountInPot=" + currAmountInPot + ", currentHand=" + currentHand + ", next=" + next
				+ ", status=" + status + "]";
	}

}