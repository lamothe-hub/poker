package com.jeffrey.project.card.model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

	List<Card> deck;
	int currCardIndex;
	
	public Deck() {
		deck = new ArrayList<Card>(); 

		for(int suit = 1; suit < 5; suit++) {
			for(int num = 1; num < 15; num++) {
				deck.add(new Card(num, suit));
			}
		}
		
		shuffle();
		
		currCardIndex = 0;

	}
	
	public List<Card> shuffle() {
		
		
		//Collections.shuffle(deck, new Random(9999999));
		
		Random r = new Random();
		int numIterations = 0;
		while(numIterations < 5) {
			for(int i = 51; i >0; i--) {
				
				int j = r.nextInt(i);
				Card temp = deck.get(i);
				deck.set(i, deck.get(j));
				deck.set(j,  temp);
				
			}
			numIterations += 1;
		}
		
		currCardIndex = 0;
		
		return deck;
	}
	
	public Card nextCard() {
		Card currCard = deck.get(currCardIndex);
		currCardIndex += 1;
		return currCard;
	}
	
	public List<Card> getDeck() {
		return deck;
	}
	
	
}
