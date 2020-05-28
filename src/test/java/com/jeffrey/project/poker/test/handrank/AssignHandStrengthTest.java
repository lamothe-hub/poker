package com.jeffrey.project.poker.test.handrank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jeffrey.project.poker.handrank.HandChecker;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;

public class AssignHandStrengthTest {

	
	private HandChecker handChecker;
	
	ArrayList<Card> communityCards;
	List<Card> flop; 
	Card turn; 
	Card river;

	
	@Before
	public void resetCards() { 
		communityCards = new ArrayList<Card>();
		handChecker = new HandChecker();
		flop = new ArrayList<Card>(); 
		turn = null; 
		river = null;
	}

	
	
	@Test
	public void highCardTestA() {
		
		String testName = "highCardTestA";
		System.out.println("\n*** Starting " + testName + " - simple ace-high test ***");
		setDeck('A');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(5, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		
		try {
			assertEquals(10, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}

				
	}
	
	@Test 
	public void highCardTestB() {
		
		String testName = "highCardTestB";
		System.out.println("\n*** Starting " + testName + " - high card is in flop ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(2, 1),
				new Card(5, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(10, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test 
	public void pairTestA() {
		
		String testName = "pairTestA";
		System.out.println("\n*** Starting " + testName + " - simple hole pair ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(2, 1),
				new Card(2, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(9, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test 
	public void pairTestB() {
		
		String testName = "pairTestB";
		System.out.println("\n*** Starting " + testName + " - bottom pair boarded ***");
		
		setDeck('C');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(13, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(9, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test 
	public void pairTestC() {
		
		String testName = "pairTestC";
		System.out.println("\n*** Starting " + testName + " - middle pair ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(10, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(9, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test 
	public void twoPairTestA() {
		
		String testName = "twoPairTestA";
		System.out.println("\n*** Starting " + testName + " - two pair boarded ***");
		
		setDeck('D');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(10, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(8, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test 
	public void twoPairTestB() {
		
		String testName = "twoPairTestB";
		System.out.println("\n*** Starting " + testName + " - one pair boarded, one in the hole ***");
		
		setDeck('B');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(14, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(8, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}

	}
	
	@Test 
	public void threeOfAKindTestA() {
		
		String testName = "threeOfAKindTestA";
		System.out.println("\n*** Starting " + testName + " - three of a kind boarded ***");
		
		setDeck('E');

		Hand testHand = new Hand(
				new Card(3, 1),
				new Card(4, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(7, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}

		
	}
	
	@Test 
	public void threeOfAKindTestB() {
		
		String testName = "threeOfAKindTestB";
		System.out.println("\n*** Starting " + testName + " - pair boarded trips with a hole card ***");
		
		setDeck('B');

		Hand testHand = new Hand(
				new Card(12, 1),
				new Card(4, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(7, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}

		
	}
	
	@Test 
	public void threeOfAKindTestC() {
		
		String testName = "threeOfAKindTestB";
		System.out.println("\n*** Starting " + testName + " - hole pair trips for a set ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(12, 1),
				new Card(12, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(7, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightTestA() {
		
		String testName = "straightTestA";
		System.out.println("\n*** Starting " + testName + " - three in a row on board ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(13, 1),
				new Card(14, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(6, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightTestB() {
		
		String testName = "straightTestB";
		System.out.println("\n*** Starting " + testName + " - another three in a row on board test ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(13, 1),
				new Card(9, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(6, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightTestC() {
		
		String testName = "straightTestC";
		System.out.println("\n*** Starting " + testName + " - straight with an over pair ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(5, 1),
				new Card(6, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(6, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightTestD() {
		
		String testName = "straightTestA";
		System.out.println("\n*** Starting " + testName + " - A-5 straight ***");
		
		setDeck('L');

		Hand testHand = new Hand(
				new Card(14, 3),
				new Card(5, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(6, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void flushTestA() {
		
		String testName = "flushTestA";
		System.out.println("\n*** Starting " + testName + " - simple flush test ***");
		
		setDeck('A');

		Hand testHand = new Hand(
				new Card(5, 1),
				new Card(8, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(5, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void flushTestB() {
		
		String testName = "flushTestB";
		System.out.println("\n*** Starting " + testName + " - flush boarded ***");
		
		setDeck('G');

		Hand testHand = new Hand(
				new Card(5, 1),
				new Card(8, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(5, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void flushTestC() {
		
		String testName = "flushTestC";
		System.out.println("\n*** Starting " + testName + " - 4 suited boarded ***");
		
		setDeck('H');

		Hand testHand = new Hand(
				new Card(5, 4),
				new Card(8, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(5, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void flushTestD() {
		
		String testName = "flushTestD";
		System.out.println("\n*** Starting " + testName + " - flush with over pair ***");
		
		setDeck('H');

		Hand testHand = new Hand(
				new Card(14, 4),
				new Card(8, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(5, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}

	@Test
	public void fullHouseTestA() {
		
		String testName = "fullHouseTestA";
		System.out.println("\n*** Starting " + testName + " - boarded full house ***");
		
		setDeck('I');

		Hand testHand = new Hand(
				new Card(5, 4),
				new Card(6, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(4, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void fullHouseTestB() {
		
		String testName = "fullHouseTestB";
		System.out.println("\n*** Starting " + testName + " - pair in the hole, trips boarded ***");
		
		setDeck('E');

		Hand testHand = new Hand(
				new Card(14, 4),
				new Card(14, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(4, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void fullHouseTestC() {
		
		String testName = "fullHouseTestC";
		System.out.println("\n*** Starting " + testName + " - pair in hole matches for set, pair boarded ***");
		
		setDeck('B');

		Hand testHand = new Hand(
				new Card(10, 4),
				new Card(10, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(4, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void quadsTestA() {
		
		String testName = "quadsTestA";
		System.out.println("\n*** Starting " + testName + " - quads boarded ***");
		
		setDeck('B');

		Hand testHand = new Hand(
				new Card(5, 4),
				new Card(6, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(3, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void quadsTestB() {
		
		String testName = "quadsTestB";
		System.out.println("\n*** Starting " + testName + " - trips boarded, hole card matches for quads ***");
		
		setDeck('E');

		Hand testHand = new Hand(
				new Card(11, 4),
				new Card(6, 3)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(3, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void quadsTestC() {
		
		String testName = "quadsTestC";
		System.out.println("\n*** Starting " + testName + " - pair boarded, pair in hole matches for quads ***");
		
		setDeck('B');

		Hand testHand = new Hand(
				new Card(12, 4),
				new Card(12, 2)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(3, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightFlushTestA() {
		
		String testName = "straightFlushTestA";
		System.out.println("\n*** Starting " + testName + " - straight flush boarded ***");
		
		setDeck('K');

		Hand testHand = new Hand(
				new Card(11, 4),
				new Card(12, 2)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(2, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void straightFlushTestB() {
		
		String testName = "straightFlushTestB";
		System.out.println("\n*** Starting " + testName + " - 3 card sf draw boarded ***");
		
		setDeck('K');

		Hand testHand = new Hand(
				new Card(8, 1),
				new Card(9, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(2, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	public void straightFlushTestC() {
		
		String testName = "straightFlushTestB";
		System.out.println("\n*** Starting " + testName + " - A-5 straight flush ***");
		
		setDeck('K');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(5, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(2, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	public void royalFlushTestA() {
		
		String testName = "royalFlushTestA";
		System.out.println("\n*** Starting " + testName + " - boarded royal flush ***");
		
		setDeck('M');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(5, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(1, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	public void royalFlushTestB() {
		
		String testName = "royalFlushTestB";
		System.out.println("\n*** Starting " + testName + " - royal flush draw ***");
		
		setDeck('M');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(13, 1)
		);
		
		int handStrength = handChecker.assignHandStrength(testHand, communityCards);
		try {
			assertEquals(1, handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(AssertionError er) {
			System.err.println(er.getMessage());
			throw er;
		}
		
	}
	
	@Test
	public void nullCardTestA() {
		
		String testName = "nullCardTestA";
		System.out.println("\n*** Starting " + testName + " - all 5 deck cards are null ***");
		
		setDeck('N');

		Hand testHand = new Hand(
				new Card(14, 1),
				new Card(13, 1)
		);
		
		
		try {
			int handStrength = handChecker.assignHandStrength(testHand, communityCards);
			System.out.println(handStrength);
			assertNotNull(handStrength);
			System.out.println("--- " + testName + " passed successfully.");

		} catch(NullPointerException ex) {
			System.err.println("Error: assignHandStrength has an unhandled NullPointerException. Please handle.");
			throw ex;
		}
		
	}
	
	
	
	
	public void setDeck(char deckChoice) {
		switch(deckChoice) {
		case 'A':
			// high card boarded
			communityCards.add(new Card(12, 1));
			communityCards.add(new Card(11, 1)); 
			communityCards.add(new Card(10, 1));
			communityCards.add(new Card(4, 2)); 
			communityCards.add(new Card(6, 3)); 
			break;
		case 'B': 
			// top pair boarded
			communityCards.add(new Card(12, 1));
			communityCards.add(new Card(12, 3)); 
			communityCards.add(new Card(10, 1));
			communityCards.add(new Card(4, 2)); 
			communityCards.add(new Card(6, 3)); 
			break;
		case 'C': 
			// bottom pair boarded
			communityCards.add(new Card(2, 1));
			communityCards.add(new Card(11, 3)); 
			communityCards.add(new Card(10, 1));
			communityCards.add(new Card(2, 2)); 
			communityCards.add(new Card(5, 3)); 
			break;
		case 'D': 
			// two pair boarded
			communityCards.add(new Card(2, 1));
			communityCards.add(new Card(11, 3)); 
			communityCards.add(new Card(11, 1));
			communityCards.add(new Card(2, 2)); 
			communityCards.add(new Card(5, 4)); 
			break;
		case 'E': 
			// three of a kind boarded
			communityCards.add(new Card(11, 2));
			communityCards.add(new Card(11, 3)); 
			communityCards.add(new Card(11, 1));
			communityCards.add(new Card(2, 2)); 
			communityCards.add(new Card(5, 3)); 
			break;
		case 'F': 
			// low straight draw w over pair
			communityCards.add(new Card(2, 2));
			communityCards.add(new Card(3, 3)); 
			communityCards.add(new Card(14, 1));
			communityCards.add(new Card(14, 2)); 
			communityCards.add(new Card(4, 3)); 
			break;	
		case 'G': 
			// boarded flush
			communityCards.add(new Card(2, 4));
			communityCards.add(new Card(3, 4)); 
			communityCards.add(new Card(14, 4));
			communityCards.add(new Card(13, 4)); 
			communityCards.add(new Card(4, 4)); 
			break;	
		case 'H': 
			// 4-card flush draw boarded
			communityCards.add(new Card(2, 4));
			communityCards.add(new Card(3, 4)); 
			communityCards.add(new Card(14, 4));
			communityCards.add(new Card(13, 4)); 
			communityCards.add(new Card(4, 1)); 
			break;	
		case 'I': 
			// boarded full house
			communityCards.add(new Card(2, 4));
			communityCards.add(new Card(2, 3)); 
			communityCards.add(new Card(2, 2));
			communityCards.add(new Card(13, 4)); 
			communityCards.add(new Card(13, 3)); 
			break;	
		case 'J': 
			// boarded quads
			communityCards.add(new Card(2, 4));
			communityCards.add(new Card(2, 3)); 
			communityCards.add(new Card(2, 2));
			communityCards.add(new Card(2, 1)); 
			communityCards.add(new Card(13, 3)); 
			break;	
		case 'K': 
			// boarded straight flush
			communityCards.add(new Card(3, 1));
			communityCards.add(new Card(4, 1)); 
			communityCards.add(new Card(5, 1));
			communityCards.add(new Card(6, 1)); 
			communityCards.add(new Card(7, 1)); 
			break;	
		case 'L': 
			// A-5 straight flush draw
			communityCards.add(new Card(2, 1));
			communityCards.add(new Card(3, 1)); 
			communityCards.add(new Card(4, 1));
			communityCards.add(new Card(7, 3)); 
			communityCards.add(new Card(7, 4)); 
			break;
		case 'M': 
			// boarded royal flush
			communityCards.add(new Card(14, 3));
			communityCards.add(new Card(11, 3)); 
			communityCards.add(new Card(13, 3));
			communityCards.add(new Card(12, 3)); 
			communityCards.add(new Card(10, 3)); 
			break;	
		case 'N': 
			// boarded royal flush
			communityCards.add(null);
			communityCards.add(null); 
			communityCards.add(null);
			communityCards.add(null); 
			communityCards.add(null); 
			break;	
		}
		
		
		
	
	}

}
