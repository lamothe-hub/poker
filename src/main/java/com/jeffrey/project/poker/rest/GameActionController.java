package com.jeffrey.project.poker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.project.poker.logic.JsonFriendlyConverter;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter.JsonFriendlyGameState;
import com.jeffrey.project.poker.model.GameState;

@RestController
@CrossOrigin
public class GameActionController {

	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired
	GameState gameState;

	@Autowired
	JsonFriendlyConverter jsonFriendlyConverter;

	@GetMapping(value = "/game/startHand", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState startHand() {
		// enters the blinds, deals the cards, sets the dealer, etc.
		gameState.startHand();
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping(value = "/game/dealCards", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState dealCards() {
		gameState.dealCards();
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping(value = "/game/dealFlop", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState dealFlop() {
		gameState.dealFlop();
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping(value = "/game/dealTurn", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState dealTurn() {
		gameState.dealTurn();
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping(value = "/game/dealRiver", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState dealRiver() {
		gameState.dealRiver();
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

}
