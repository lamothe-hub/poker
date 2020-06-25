package com.jeffrey.project.poker.logic;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.poker.exceptions.PlayerDoesNotExistException;
import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;
import com.jeffrey.project.poker.model.player.Player;
import com.jeffrey.project.poker.rest.StateController;

@Component
public class JsonFriendlyConverter {
	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired 
	GameState gameState;
	
	public JsonFriendlyConverter() {
	}

	public class JsonFriendlyPlayer {
		String name;
		double chipCount;
		double currAmountThisRound;
		double currAmountInPot;
		Hand currentHand;
		String status;
		String nextPlayer;

		public JsonFriendlyPlayer() {
		}

		public JsonFriendlyPlayer(Player player) {
			super();
			this.name = player.getName();
			this.chipCount = player.getChipCount();
			this.currAmountThisRound = player.getCurrAmountThisRound();
			this.currAmountInPot = player.getCurrAmountInPot();
			this.currentHand = player.getCurrentHand().clone();
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
		double mostRecentBetSize;
		String mostRecentActionReset; 
		String currTurn;

		public JsonFriendlyGameState() {
		}

		public JsonFriendlyGameState(GameState badlyFormattedState) {
			List<JsonFriendlyPlayer> currJsonFriendlyList = new ArrayList<JsonFriendlyPlayer>();

			Player currPlayer = badlyFormattedState.getPlayersList().getMaster();
			if (currPlayer != null) {
				do {
					currJsonFriendlyList.add(new JsonFriendlyPlayer(currPlayer));
					currPlayer = currPlayer.getNext();
				} while (currPlayer != badlyFormattedState.getPlayersList().getMaster());
			}
		
			this.runStatus = badlyFormattedState.getRunStatus();
			this.playersList = currJsonFriendlyList;
			this.dealerName = badlyFormattedState.getPlayersList().getDealer().getName();
			this.masterName = badlyFormattedState.getPlayersList().getMaster().getName();
			this.flop = badlyFormattedState.getFlop();
			this.turn = badlyFormattedState.getTurn();
			this.river = badlyFormattedState.getRiver();
			this.pot = badlyFormattedState.getPot();
			this.mostRecentBetSize = badlyFormattedState.getMostRecentBetSize();
			if(badlyFormattedState.getMostRecentActionReset() == null) {
				this.mostRecentActionReset = null;
			} else {
				this.mostRecentActionReset = badlyFormattedState.getMostRecentActionReset().getName(); 
			}
			if(badlyFormattedState.getCurrTurn() == null) {
				this.currTurn = null;
			} else {
				this.currTurn = badlyFormattedState.getCurrTurn().getName();
			}
		}

		public void hideOtherPlayersCards(String playerName, int token) throws PlayerDoesNotExistException {
			boolean playerExists = false;
			boolean isEndOfRound = false;
			if(gameState.getRunStatus().equals("end")) {
				isEndOfRound = true;
			}
			if(playersList != null) {
				for(JsonFriendlyPlayer player: playersList) {
					if(player.getName().equals(playerName) && gameState.getPlayersList().getPlayerByName(playerName).getHashCode() == token) {
						playerExists = true;
					} else {
						if(!isEndOfRound) {
							player.getCurrentHand().wipeHand();
						}
					}
				}
			}
			if(!playerExists) {
				throw new PlayerDoesNotExistException(playerName + " - " + token);
			}
			
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

		public double getMostRecentBetSize() {
			return mostRecentBetSize;
		}

		public void setMostRecentBetSize(double mostRecentBetSize) {
			this.mostRecentBetSize = mostRecentBetSize;
		}

		public void setPot(double pot) {
			this.pot = pot;
		}

		public String getMostRecentActionReset() {
			return mostRecentActionReset;
		}

		public void setMostRecentActionReset(String mostRecentActionReset) {
			this.mostRecentActionReset = mostRecentActionReset;
		}

		public String getCurrTurn() {
			return currTurn;
		}

		public void setCurrTurn(String currTurn) {
			this.currTurn = currTurn;
		}
		
		
		

	}

	public JsonFriendlyGameState convert(GameState badlyFormattedState) {
		return new JsonFriendlyGameState(badlyFormattedState);
	}

}
