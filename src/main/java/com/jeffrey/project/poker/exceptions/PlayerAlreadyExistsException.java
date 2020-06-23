package com.jeffrey.project.poker.exceptions;

public class PlayerAlreadyExistsException extends Exception{

	public PlayerAlreadyExistsException(String playerName) {
		super("A player with the name of [ " + playerName + " ] already exists...");
	}
}
