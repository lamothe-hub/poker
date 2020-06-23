package com.jeffrey.project.poker.exceptions;

public class InvalidGameStateException extends Exception {

	public InvalidGameStateException() {
		super("Something went wrong. Either the game has not started or there is in invalid number of players..");
	}
}
