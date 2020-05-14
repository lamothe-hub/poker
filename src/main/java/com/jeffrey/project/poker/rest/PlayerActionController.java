package com.jeffrey.project.poker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.project.poker.logic.ActionValidator;
import com.jeffrey.project.poker.logic.GameStateManager;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter.JsonFriendlyGameState;
import com.jeffrey.project.poker.model.GameState;

@RestController
@CrossOrigin
public class PlayerActionController {

	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

//    @Autowired
//    GameState gameState;

	@Autowired
	ActionValidator actionValidator;

	@Autowired
	GameStateManager gameStateManager;

	@Autowired
	JsonFriendlyConverter jsonFriendlyConverter;

	@Autowired
	GameState gameState;

	@GetMapping("/action/{playerName}/bet/{amount}")
	public JsonFriendlyGameState bet(@PathVariable String playerName, @PathVariable double amount) {
		gameStateManager.placeBet(playerName, amount);
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping("/action/{playerName}/call/{amount}")
	public JsonFriendlyGameState call(@PathVariable String playerName, @PathVariable double amount) {
		gameStateManager.makeCall(playerName, amount);
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping("/action/{playerName}/check")
	public JsonFriendlyGameState check(@PathVariable String playerName) {
		gameStateManager.check(playerName);
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

	@GetMapping("/action/{playerName}/fold")
	public JsonFriendlyGameState fold(@PathVariable String playerName) {
		gameStateManager.fold(playerName);
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

}
