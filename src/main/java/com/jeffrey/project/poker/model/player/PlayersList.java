package com.jeffrey.project.poker.model.player;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeffrey.project.poker.rest.StateController;

/*
 * PlayersLL is a linked list of all of the player objects
 * master is the first person to "join the table" and will have special control over games. 
 * 	-master will not change unless master leaves the room
 * dealer will rotate around the table as the game progresses
 */

public class PlayersList {
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	private Player master;
	private Player dealer;
	// private Player currTurn;

	public PlayersList() {
		dealer = null;
		master = null;
	}

	public void addPlayer(String name, double chipCount) {
		// TODO: Check if player with that name exists before adding- throw error;
		Player newPlayer = new Player(name, chipCount);

		if (master == null) {
			master = newPlayer;
			dealer = newPlayer;
			master.next = newPlayer;
		} else {
			Player currPlayer = master;
			while (currPlayer.next != master) {
				currPlayer = currPlayer.next;
			}
			currPlayer.next = newPlayer;
			newPlayer.next = master;
		}
	}

	public void setAllToWaiting() {
		Player currPlayer = dealer;
		do {
			currPlayer.setToWaiting();
			currPlayer = currPlayer.next;
		} while (currPlayer != dealer);
	}

	public void removePlayer(String name) {
		Player currPlayer = master;
		if (currPlayer.next == master) {
			// There is only one player in the game - remove and set everything to null

			master = null;
			dealer = null;

		} else if (currPlayer.getName().equals(name)) {
			// The player is master - must reassign a new master and point last player to
			// new master

			Player lastInList = master;
			while (lastInList.next != master) {
				lastInList = lastInList.next;
			}
			lastInList.next = master.next; // set lastInList to the player AFTER master
			master = currPlayer.next;
			if (currPlayer == dealer) {
				dealer = currPlayer.next;
			}

		} else {
			// If we've made it this far, we are NOT removing the master node.
			// Handle all other cases ->
			currPlayer = currPlayer.next;
			while (!currPlayer.next.getName().equals(name) & currPlayer != master) {
				currPlayer = currPlayer.next;
			}
			if (currPlayer == master) {
				// TODO: throw exception: that name is not in the list
				logger.warn(name + " is not in the players list. Can not remove..");
			} else {
				currPlayer.next = currPlayer.next.next;
				if (currPlayer.next == dealer) {
					dealer = currPlayer.next.next;
				}
			}

		}
	}

	public int getPlayerCount() {
		Player currPlayer = master;
		if (master == null) {
			return 0;
		}
		int counter = 1;
		currPlayer = currPlayer.next;
		while (currPlayer != master) {
			currPlayer = currPlayer.next;
			counter++;
		}
		return counter;
	}

	public List<String> getPlayerOrder(String currentPlayerName) {
		// Pass in the name of the current player to return the order of players
		// starting with this player
		List playerList = new ArrayList<String>();
		Player currPlayer = master;
		while (!currPlayer.getName().equals(currentPlayerName)) {
			currPlayer = currPlayer.next;
		}
		do {
			playerList.add(currPlayer.getName());
			currPlayer = currPlayer.next;
		} while (currPlayer.getName() != currentPlayerName);

		return playerList;
	}

	public void printList() {
		Player currPlayer = master;
		System.out.println("---------------------");
		do {
			System.out.println(currPlayer.getName() + " ---> " + currPlayer.next.getName());
			currPlayer = currPlayer.next;
		} while (currPlayer != master);
		System.out.println("---------------------");
	}

	public Player getPlayerByName(String name) {
		Player currPlayer = master;
		if (currPlayer == null) {
			return null;
		}
		do {
			if (currPlayer.getName().equals(name)) {
				return currPlayer;
			}
			currPlayer = currPlayer.next;
		} while (currPlayer != master);

		return null; // user does not exist
	}
	
	public List<Player> getAllPlayersInList() {
		Player currPlayer = dealer;
		List<Player> currList = new ArrayList<Player>();
		do {
			currList.add(currPlayer); 
			currPlayer = currPlayer.getNext();
		} while(currPlayer != dealer);
		return currList;
	}
	
	public List<Player> getPlayersInPlay() {
		Player currPlayer = dealer;
		List<Player> currList = new ArrayList<Player>();
		do {
			if(currPlayer.inHand()) {
				currList.add(currPlayer); 
			}
			currPlayer = currPlayer.getNext();
		} while(currPlayer != dealer);
		return currList;
	}

	public Player getMaster() {
		return master;
	}

	public void setMaster(Player master) {
		this.master = master;
	}

	public Player getDealer() {
		return dealer;
	}
	
	

	public void setDealer(Player dealer) {
		this.dealer = dealer;
	}

	@Override
	public String toString() {

		return "PlayersList [master=" + master.getName() + ", dealer=" + dealer.getName() + "]";
	}
}
