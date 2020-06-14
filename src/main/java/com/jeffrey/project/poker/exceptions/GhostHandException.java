package com.jeffrey.project.poker.exceptions;

public class GhostHandException extends Exception {

	private static final long serialVersionUID = -3068457648567608113L;

	public GhostHandException() {
		super("There are one or zero players in this hand. There is an error in the gamestate logic.");
	}
}
