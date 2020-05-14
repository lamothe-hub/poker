package com.jeffrey.project.poker.exceptions;

public class InvalidCheckException extends RuntimeException {

	private static final long serialVersionUID = 4804985193159064385L;

	public InvalidCheckException(String playerName) {
		super(playerName + " is not allowed to check right now.");
	}
}
