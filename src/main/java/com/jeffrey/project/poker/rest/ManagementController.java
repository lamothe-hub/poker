package com.jeffrey.project.poker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.project.poker.exceptions.PlayerAlreadyExistsException;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter.JsonFriendlyGameState;
import com.jeffrey.project.poker.model.GameState;

@RestController
@CrossOrigin
public class ManagementController {
	
	private static final Logger logger = LoggerFactory.getLogger(ManagementController.class);
	
	@Autowired
	GameState gameState;

	@Autowired
	JsonFriendlyConverter jsonFriendlyConverter;
	
	@GetMapping(value = "/management/addPlayer/{playerName}/{buyinAmount}", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState addPlayer(@PathVariable String playerName, @PathVariable double buyinAmount) {
		// enters the blinds, deals the cards, sets the dealer, etc.
		try {
			gameState.addPlayer(playerName, buyinAmount);
		} catch(PlayerAlreadyExistsException ex) {
			logger.error(ex.getMessage());
		}
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}
}
