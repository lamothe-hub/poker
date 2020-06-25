package com.jeffrey.project.poker.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/action/{playerName}/{token}/bet/{amount}")
	public ResponseEntity<?> bet(@PathVariable String playerName, @PathVariable double amount, @PathVariable int token) {
		
		ResponseEntity<?> response;
		
		try {
			gameStateManager.placeBet(playerName, token, amount);
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			jsonFriendlyGameState.hideOtherPlayersCards(playerName, token);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState, HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
	
		return response;
		
	}

	@GetMapping("/action/{playerName}/{token}/call/{amount}")
	public ResponseEntity<?> call(@PathVariable String playerName, @PathVariable double amount, @PathVariable int token) {
		
		ResponseEntity response;
		
		try {
			gameStateManager.makeCall(playerName, token, amount);
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			jsonFriendlyGameState.hideOtherPlayersCards(playerName, token);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState,HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
		return response;
	}

	@GetMapping("/action/{playerName}/{token}/check")
	public ResponseEntity<?> check(@PathVariable String playerName, @PathVariable int token) {
		
		ResponseEntity<?> response; 
		
		try {
			gameStateManager.check(playerName, token);
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			jsonFriendlyGameState.hideOtherPlayersCards(playerName, token);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState, HttpStatus.ACCEPTED);
			
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
		
		return response;
	}

	@GetMapping("/action/{playerName}/{token}/fold")
	public ResponseEntity<?> fold(@PathVariable String playerName, @PathVariable int token) {
		
		ResponseEntity<?> response;
		try {
			gameStateManager.fold(playerName, token);
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			jsonFriendlyGameState.hideOtherPlayersCards(playerName, token);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState, HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
		
		return response;
	}
	
}
