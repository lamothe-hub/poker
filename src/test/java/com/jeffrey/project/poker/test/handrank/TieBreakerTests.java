package com.jeffrey.project.poker.test.handrank;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.jeffrey.project.poker.handrank.HandChecker;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;
import com.jeffrey.project.poker.model.player.Player;

public class TieBreakerTests {

	private HandChecker handChecker;
	ArrayList<Card> flop;
	ArrayList<Player> allPlayers;
	Card turn;
	Card river;
	
	@Before
	public void resetCards() {
		handChecker = new HandChecker();
		flop = new ArrayList<Card>();
		turn = new Card();
		river = new Card();
		allPlayers = new ArrayList<Player>();
	}
	
	
	@Test
	public void highCardtestA() {
		String testName = "highCardTestA";
		System.out.println("\n*** Starting " + testName + " High card rankings between 5 players ***");
		
		setDeck('A');
		setPlayers(5);
		
		ArrayList<ArrayList<Player>> playersList = handChecker.determineWinner(allPlayers, flop, turn, river);
		
		
		//for(int i = 0; i < playersList.size())
		//System.out.println("SET FUNCTIONS WORK");
		try {
			assert(checkPlayerLocation(playersList, 0, "Johnny")
					&& checkPlayerLocation(playersList, 0, "Mark") 
					&& checkPlayerLocation(playersList, 0, "Jeff")
					&& checkPlayerLocation(playersList, 0, "Jared")
					&& checkPlayerLocation(playersList, 1, "Proportional"));
			System.out.println("--- " + testName + " passed successfully.");
		}
		catch (AssertionError e){
			System.out.println(e.getMessage());
			throw(e);
		}
	}
	
	
	public boolean checkPlayerLocation(ArrayList<ArrayList<Player>> playersList, int index, String name){
		for(int i = 0; i < playersList.get(index).size(); i++) {
			if(playersList.get(index).get(i).getName() == name)
				return true;
		}
		return false;
	}
	
	
	public void setDeck(char deckChoice) {
		switch(deckChoice) {
		//Boarded Royal
		case 'A' :
			flop.add(new Card(14, 1));
			flop.add(new Card(13, 1));
			flop.add(new Card(12, 1));
			turn = (new Card(11, 1));
			river = (new Card(7, 1));			
			break;
		}
	}
	
	public void setPlayers(int numPlayers) {
		switch(numPlayers) {
		case 1 :
			allPlayers.add(new Player("Johnny"));
			allPlayers.get(0).setCurrentHand(new Hand());
			break;
		case 2 :
			allPlayers.add(new Player("Johnny"));
			allPlayers.add(new Player("Mark"));
			break;
		case 3 :
			allPlayers.add(new Player("Johnny"));
			allPlayers.add(new Player("Mark"));
			allPlayers.add(new Player("Jeff"));
			break;
		case 4 :
			allPlayers.add(new Player("Johnny"));
			allPlayers.add(new Player("Mark"));
			allPlayers.add(new Player("Jeff"));
			allPlayers.add(new Player("Jared"));
			break;
		case 5 :
			allPlayers.add(new Player("Johnny"));
			allPlayers.get(0).setCurrentHand(new Hand(new Card(10, 1),new Card (3, 2)));
			allPlayers.add(new Player("Mark"));
			allPlayers.get(1).setCurrentHand(new Hand(new Card(10, 1),new Card (3, 3)));
			allPlayers.add(new Player("Jeff"));
			allPlayers.get(2).setCurrentHand(new Hand(new Card(10, 1),new Card (3, 4)));
			allPlayers.add(new Player("Jared"));
			allPlayers.get(3).setCurrentHand(new Hand(new Card(10, 1),new Card (3, 1)));
			allPlayers.add(new Player("Proportional"));
			allPlayers.get(4).setCurrentHand(new Hand(new Card(4,2),new Card (5, 2)));
			break;
		}
	}
}
















