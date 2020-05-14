package com.jeffrey.project.poker.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jeffrey.project.poker.exceptions.InvalidBetException;
import com.jeffrey.project.poker.exceptions.InvalidCallException;
import com.jeffrey.project.poker.exceptions.InvalidCheckException;
import com.jeffrey.project.poker.exceptions.InvalidFoldException;
import com.jeffrey.project.poker.exceptions.OutOfTurnException;
import com.jeffrey.project.poker.exceptions.PlayerDoesNotExistException;
import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.player.Player;

@Component
public class ActionValidator {

	/*
	 * whereas GameState component keeps track of the state and offers methods to
	 * manipulate the data, GameStateManager is called by the controller methods to
	 * make sure that the requested changes are valid (i.e. it is the proper
	 * player's turn; the player has enough chips to make that bet size; etc.)
	 */

	@Autowired
	GameState gameState;

	public Player isValidBet(String playerName, double amount) {
		// check if it is that player's turn and if it is a valid betsize
		Player player = gameState.getPlayersList().getPlayerByName(playerName);
		if (player == null)
			throw (new PlayerDoesNotExistException(playerName));
		if (!isPlayersTurn(player))
			throw (new OutOfTurnException(player));
		if (!isValidBetSize(player, amount))
			throw (new InvalidBetException(playerName, amount));
		return player;
	}

	public Player isValidCall(String playerName, double amount) {
		Player player = gameState.getPlayersList().getPlayerByName(playerName);
		if (player == null)
			throw (new PlayerDoesNotExistException(playerName));
		if (!isPlayersTurn(player))
			throw (new OutOfTurnException(player));
		if (!isValidCallSize(player, amount))
			throw (new InvalidCallException(playerName, amount));
		return player;
	}

	public Player isValidCheck(String playerName) {
		Player player = gameState.getPlayersList().getPlayerByName(playerName);
		if (player == null)
			throw (new PlayerDoesNotExistException(playerName));
		if (!isPlayersTurn(player))
			throw (new OutOfTurnException(player));
		if (!alreadyBetOrCalled(player))
			throw (new InvalidCheckException(playerName));
		return player;
	}

	public Player isValidFold(String playerName) {
		Player player = gameState.getPlayersList().getPlayerByName(playerName);
		if (player == null)
			throw (new PlayerDoesNotExistException(playerName));
		if (!isPlayersTurn(player))
			throw (new OutOfTurnException(player));
		if (player.getStatus() == "allIn")
			throw (new InvalidFoldException(playerName));
		return player;
	}

	public boolean alreadyBetOrCalled(Player player) {
		if (player.getCurrAmountThisRound() == gameState.getMostRecentBetSize()) {
			return true;
		}
		return false;
	}

	public boolean isValidCallSize(Player player, double amount) {
		if (amount <= player.getChipCount()) {
			if (amount == gameState.getMostRecentBetSize() - player.getCurrAmountThisRound()) {
				return true;
			} else if (amount == player.getChipCount() - player.getCurrAmountThisRound()) {
				return true;
			}
		}
		return false;
	}

	public boolean isPlayersTurn(Player player) {
		if (gameState.getCurrTurn().getName().equals(player.getName())) {
			return true;
		}
		return false;
	}

	public boolean isValidBetSize(Player player, double amount) {
		if (amount - player.getCurrAmountThisRound() > player.getChipCount()) {
			return false;
		}
		if (amount - player.getCurrAmountThisRound() >= gameState.getBigBlind() + gameState.getMostRecentBetSize()) {
			return true;
		} else if (amount == player.getChipCount()) {
			return true;
		}
		return false;
	}

}
