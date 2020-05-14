package com.jeffrey.project.card.model.card;

public class Hand {

	Card cardA;
	Card cardB; 
	
	public Hand() {
		this.cardA = null;
		this.cardB = null;
	}
	
	public void setCardA (Card card) {
		cardA = card;
	}
	
	public void setCardB (Card card) {
		cardB = card;
	}
	
	public Card getCardA() {
		return cardA;
	}
	
	public Card getCardB() {
		return cardB;
	}
}