package com.jeffrey.project.poker.exceptions;

public class InvalidBetException extends RuntimeException {

	private static final long serialVersionUID = -2570358391079527502L;

	public InvalidBetException(String playerName, double amount) {
		super(playerName + " can not place a bet of " + amount + " at this time");
	}

}
