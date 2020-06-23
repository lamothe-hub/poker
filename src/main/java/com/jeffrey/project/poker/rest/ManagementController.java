package com.jeffrey.project.poker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> addPlayer(@PathVariable String playerName, @PathVariable double buyinAmount) {
		// enters the blinds, deals the cards, sets the dealer, etc.
		ResponseEntity<?> response;
		try {
			gameState.addPlayer(playerName, buyinAmount);		
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState,HttpStatus.ACCEPTED);
		} catch(PlayerAlreadyExistsException ex) {
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			response = new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return response;

	}
	
	
	@GetMapping(value = "/management/createGame/{playerName}/{buyinAmount}/{maxBuyIn}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createGame(@PathVariable String playerName, @PathVariable double buyinAmount, @PathVariable double maxBuyIn) {
		ResponseEntity<?> response;
		try {
			gameState.reset();
			gameState.addPlayer(playerName, buyinAmount);
			gameState.setMaxBuyIn(maxBuyIn);
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState,HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			response = new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return response;
	}
}
