package com.jeffrey.project.poker.model.card;

public class Card {

	private int number; // Jack: 11; Queen: 12; King: 13; Ace: 14
	private int suit; // 1: spades; 2: hearts; 3: clubs; 4: diamonds;

	public Card() {
	}

	public Card(int number, int suit) {
		this.number = number;
		this.suit = suit;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}
	
	public String toString() {
		return number + " of " + suit;
	}
	
	public Card clone() {
		Card cardClone = new Card(); 
		cardClone.setNumber(number);
		cardClone.setSuit(suit);
		return cardClone;
	}

}
