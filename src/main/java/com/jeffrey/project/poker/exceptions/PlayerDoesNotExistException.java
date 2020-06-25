package com.jeffrey.project.poker.exceptions;

public class PlayerDoesNotExistException extends RuntimeException {

	private static final long serialVersionUID = 501883316371687875L;

	public PlayerDoesNotExistException(String playerName) {
		super("[ " + playerName + " ] is not a valid playerName - token combination");
	}

}
