package com.jeffrey.project.poker.exceptions;

import com.jeffrey.project.poker.model.player.Player;

public class PotEmptyException extends Exception {

	public PotEmptyException(Player player, double amount, double potSize) {
		super("Can not transfer [ " + amount + " ] to [ " + player.getName() + " ]. Pot only contains [ " + potSize + " ]." );
	}
}
