package com.jeffrey.project.poker.model.card;

public class Hand {

	Card cardA;
	Card cardB;

	public Hand() {
		this.cardA = null;
		this.cardB = null;
	}

	public Hand(Card cardA, Card cardB) {
		this.cardA = cardA; 
		this.cardB = cardB;
	}

	public void setCardA(Card card) {
		cardA = card;
	}

	public void setCardB(Card card) {
		cardB = card;
	}

	public Card getCardA() {
		return cardA;
	}

	public Card getCardB() {
		return cardB;
	}
	
	public Hand clone() {
		Hand handClone;
		if(cardA != null && cardB != null) {
			Card cloneCardA = cardA.clone(); 
			Card cloneCardB = cardB.clone();
			handClone = new Hand(cloneCardA, cloneCardB);
		} else {
			handClone = new Hand();
		}
		
		return handClone;
	}
	
	public void wipeHand() {
		cardA = null; 
		cardB = null;
	}
}