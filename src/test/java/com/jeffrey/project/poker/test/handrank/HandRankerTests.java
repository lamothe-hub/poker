package com.jeffrey.project.poker.test.handrank;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.jeffrey.project.poker.handrank.HandChecker;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;

public class HandRankerTests {
	
	private static final int ROYAL_FLUSH       = 9000000;
	private static final int STRAIGHT_FLUSH    = 8000000; // + highCard
	private static final int FOUR_OF_A_KIND    = 7000000; // + (Fours Card Rank * 15) + highCard
	private static final int FULL_HOUSE        = 6000000; // + (3's Value * 15) + 2's Value
	private static final int FLUSH             = 5000000; // + highCard
	private static final int STRAIGHT          = 4000000; // + highCard
	private static final int THREE_OF_A_KIND   = 3000000; // + (3's value * 211) + ( High1 * 15 ) + High2
	private static final int TWO_PAIR          = 2000000; // + (High * 211) + (Low * 15) + card
	private static final int ONE_PAIR          = 1000000; // + (pair * 2955) + high1 * 211 + high2 * 15 + high3


	private HandChecker handChecker;
	ArrayList<Card> playableCards;
	
	@Before
	public void resetCards() {
		handChecker = new HandChecker();
		playableCards = new ArrayList<Card>();
	}

	
	@Test
	public void straightFlushRankerTestA() {
		String testName = "straightFlushRankerTestA";
		System.out.println("\n*** Starting " + testName + " - Q high straight flush ***");
		
		setDeck('A');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(8, 1));
		playableCards.add(new Card(9, 1));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(STRAIGHT_FLUSH + 12, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	@Test
	public void straightFlushRankerTestB() {
		String testName = "straightFlushRankerTestB";
		System.out.println("\n*** Starting " + testName + " - A-5 straight flush ***");
		
		setDeck('L');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(1, 1));
		playableCards.add(new Card(14, 1));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(STRAIGHT_FLUSH + 5, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	@Test
	public void quadsRankerTestA() {
		String testName = "quadsRankerTestA";
		System.out.println("\n*** Starting " + testName + " - quad 2's + King high ***");
		
		setDeck('J');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(5, 2));
		playableCards.add(new Card(6, 3));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(FOUR_OF_A_KIND + 13, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	@Test
	public void fullHouseRankerTestA() {
		String testName = "fullHouseRankerTestA";
		System.out.println("\n*** Starting " + testName + " - three 2's, two Kings ***");
		
		setDeck('I');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(5, 2));
		playableCards.add(new Card(6, 3));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(FULL_HOUSE + (2 * 15) + 13, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	@Test
	public void straightRankerTestA() {
		String testName = "straightRankerTestA";
		System.out.println("\n*** Starting " + testName + " - straight 8-Q ***");
		
		setDeck('A');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(8, 2));
		playableCards.add(new Card(9, 3));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(STRAIGHT + 12, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	@Test
	public void straightRankerTestB() {
		String testName = "straightRankerTestB";
		System.out.println("\n*** Starting " + testName + " - A-5 straight ***");
		
		setDeck('L');

		// add two more playable cards for hole cards:
		playableCards.add(new Card(14, 1));
		playableCards.add(new Card(5, 1));
		
		int handScore = handChecker.straitFlushRanker(playableCards);
		
		try {
			assertEquals(STRAIGHT + 12, handScore);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
	}
	
	
	public void setDeck(char deckChoice) {
		switch(deckChoice) {
		case 'A':
			// high card boarded
			playableCards.add(new Card(12, 1));
			playableCards.add(new Card(11, 1)); 
			playableCards.add(new Card(10, 1));
			playableCards.add(new Card(4, 2)); 
			playableCards.add(new Card(6, 3)); 
			break;
		case 'B': 
			// top pair boarded
			playableCards.add(new Card(12, 1));
			playableCards.add(new Card(12, 3)); 
			playableCards.add(new Card(10, 1));
			playableCards.add(new Card(4, 2)); 
			playableCards.add(new Card(6, 3)); 
			break;
		case 'C': 
			// bottom pair boarded
			playableCards.add(new Card(2, 1));
			playableCards.add(new Card(11, 3)); 
			playableCards.add(new Card(10, 1));
			playableCards.add(new Card(2, 2)); 
			playableCards.add(new Card(5, 3)); 
			break;
		case 'D': 
			// two pair boarded
			playableCards.add(new Card(2, 1));
			playableCards.add(new Card(11, 3)); 
			playableCards.add(new Card(11, 1));
			playableCards.add(new Card(2, 2)); 
			playableCards.add(new Card(5, 4)); 
			break;
		case 'E': 
			// three of a kind boarded
			playableCards.add(new Card(11, 2));
			playableCards.add(new Card(11, 3)); 
			playableCards.add(new Card(11, 1));
			playableCards.add(new Card(2, 2)); 
			playableCards.add(new Card(5, 3)); 
			break;
		case 'F': 
			// low straight draw w over pair
			playableCards.add(new Card(2, 2));
			playableCards.add(new Card(3, 3)); 
			playableCards.add(new Card(14, 1));
			playableCards.add(new Card(14, 2)); 
			playableCards.add(new Card(4, 3)); 
			break;	
		case 'G': 
			// boarded flush
			playableCards.add(new Card(2, 4));
			playableCards.add(new Card(3, 4)); 
			playableCards.add(new Card(14, 4));
			playableCards.add(new Card(13, 4)); 
			playableCards.add(new Card(4, 4)); 
			break;	
		case 'H': 
			// 4-card flush draw boarded
			playableCards.add(new Card(2, 4));
			playableCards.add(new Card(3, 4)); 
			playableCards.add(new Card(14, 4));
			playableCards.add(new Card(13, 4)); 
			playableCards.add(new Card(4, 1)); 
			break;	
		case 'I': 
			// boarded full house
			playableCards.add(new Card(2, 4));
			playableCards.add(new Card(2, 3)); 
			playableCards.add(new Card(2, 2));
			playableCards.add(new Card(13, 4)); 
			playableCards.add(new Card(13, 3)); 
			break;	
		case 'J': 
			// boarded quads
			playableCards.add(new Card(2, 4));
			playableCards.add(new Card(2, 3)); 
			playableCards.add(new Card(2, 2));
			playableCards.add(new Card(2, 1)); 
			playableCards.add(new Card(13, 3)); 
			break;	
		case 'K': 
			// boarded straight flush
			playableCards.add(new Card(3, 1));
			playableCards.add(new Card(4, 1)); 
			playableCards.add(new Card(5, 1));
			playableCards.add(new Card(6, 1)); 
			playableCards.add(new Card(7, 1)); 
			break;	
		case 'L': 
			// A-5 straight flush draw
			playableCards.add(new Card(2, 1));
			playableCards.add(new Card(3, 1)); 
			playableCards.add(new Card(4, 1));
			playableCards.add(new Card(7, 3)); 
			playableCards.add(new Card(7, 4)); 
			break;
		case 'M': 
			// boarded royal flush
			playableCards.add(new Card(14, 3));
			playableCards.add(new Card(11, 3)); 
			playableCards.add(new Card(13, 3));
			playableCards.add(new Card(12, 3)); 
			playableCards.add(new Card(10, 3)); 
			break;	
		case 'N': 
			// boarded royal flush
			playableCards.add(null);
			playableCards.add(null); 
			playableCards.add(null);
			playableCards.add(null); 
			playableCards.add(null); 
			break;	
		}
	}

}
