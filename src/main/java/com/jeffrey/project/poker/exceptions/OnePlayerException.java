package com.jeffrey.project.poker.exceptions;

import com.jeffrey.project.poker.model.player.Player;

public class OnePlayerException extends Exception {
 
	public OnePlayerException(Player player) {
		super(player.getName() + " is the only player in the lobby.");
	}
	
	
}
