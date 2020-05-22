package com.jeffrey.project.poker.model.player;

import com.jeffrey.project.poker.exceptions.InvalidCallException;
import com.jeffrey.project.poker.model.card.Hand;

public class Player {

	String name;
	double chipCount;
	double currAmountThisRound;
	double currAmountInPot;
	Hand currentHand;
	public Player next;
	String status; 

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
		realAmount = mostRecentBetSize - currAmountThisRound;
		if(realAmount != amount) {
			throw(new InvalidCallException(name, amount));
		}
		chipCount -= realAmount; 
		currAmountThisRound += realAmount; 
		currAmountInPot += realAmount; 
		setToCalled();
		return realAmount;
		
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

	public void setCurrAmountInPot(double currAmountInPot) {
		this.currAmountInPot = currAmountInPot;
	}
	public Hand getHand() {
		return currentHand;
	}

	@Override
	public String toString() {
		return "Player [name=" + name + ", chipCount=" + chipCount + ", currAmountThisRound=" + currAmountThisRound
				+ ", currAmountInPot=" + currAmountInPot + ", currentHand=" + currentHand + ", next=" + next
				+ ", status=" + status + "]";
	}

}