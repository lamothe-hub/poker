package com.jeffrey.project.card.logic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.jeffrey.project.card.model.GameState;
import com.jeffrey.project.card.model.card.Card;
import com.jeffrey.project.card.model.card.Hand;
import com.jeffrey.project.card.model.player.Player;

@Component
public class JsonFriendlyConverter { 
	
	public JsonFriendlyConverter() {}

	public class JsonFriendlyPlayer {
		String name;
		double chipCount;
		double currAmountThisRound;
		double currAmountInPot; 		
		Hand currentHand;
		String status; 
		String nextPlayer; 
		
		public JsonFriendlyPlayer () {}
		

		public JsonFriendlyPlayer(Player player) {
			super();
			this.name = player.getName();
			this.chipCount = player.getChipCount();
			this.currAmountThisRound = player.getCurrAmountThisRound();
			this.currAmountInPot = player.getCurrAmountInPot();
			this.currentHand = player.getCurrentHand();
			this.status = player.getStatus();
			this.nextPlayer = player.getNext().getName();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

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

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
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
		
		public String getNextPlayer() {
			return nextPlayer;
		}
		
		public void setNextPlayer(String nextPlayer) {
			this.nextPlayer = nextPlayer;
		}

	} 
	
	public class JsonFriendlyGameState {

		List<JsonFriendlyPlayer> playersList;
		String runStatus; // running, paused, notStarted
		String dealerName; 
		String masterName;
		List<Card> flop;
		Card turn;
		Card river;
		double pot;
		
		public JsonFriendlyGameState () {}
		
		public JsonFriendlyGameState(GameState badlyFormattedState) { 
			List<JsonFriendlyPlayer> currJsonFriendlyList = new ArrayList<JsonFriendlyPlayer>(); 
			
			Player currPlayer = badlyFormattedState.getPlayersList().getMaster(); 
			if(currPlayer != null) {
				do {
					currJsonFriendlyList.add(new JsonFriendlyPlayer(currPlayer));
					currPlayer = currPlayer.getNext();
				} while(currPlayer != badlyFormattedState.getPlayersList().getMaster());
			} 
			 
			this.runStatus = badlyFormattedState.getRunStatus(); 
			this.playersList = currJsonFriendlyList;
			this.dealerName = badlyFormattedState.getPlayersList().getDealer().getName(); 
			this.masterName = badlyFormattedState.getPlayersList().getMaster().getName();
			this.flop = badlyFormattedState.getFlop();
			this.turn = badlyFormattedState.getTurn();
			this.river = badlyFormattedState.getRiver();
			this.pot = badlyFormattedState.getPot();
		}
		public List<JsonFriendlyPlayer> getPlayersList() {
			return playersList;
		}

		public void setPlayersList(List<JsonFriendlyPlayer> playersList) {
			this.playersList = playersList;
		}

		public String getRunStatus() {
			return runStatus;
		}

		public void setRunStatus(String runStatus) {
			this.runStatus = runStatus;
		}
		
		public String getDealerName() {
			return dealerName;
		}

		public void setDealerName(String dealerName) {
			this.dealerName = dealerName;
		}

		public String getMasterName() {
			return masterName;
		}

		public void setMasterName(String masterName) {
			this.masterName = masterName;
		}

		public List<Card> getFlop() {
			return flop;
		}

		public void setFlop(List<Card> flop) {
			this.flop = flop;
		}

		public Card getTurn() {
			return turn;
		}

		public void setTurn(Card turn) {
			this.turn = turn;
		}

		public Card getRiver() {
			return river;
		}

		public void setRiver(Card river) {
			this.river = river;
		}

		public String toString() {
			return ("runStatus: " + runStatus);
		}
		
		public double getPot() {
			return pot;
		}
		
	} 
	
	public JsonFriendlyGameState convert(GameState badlyFormattedState) {
		return new JsonFriendlyGameState(badlyFormattedState);
	}
	
}
