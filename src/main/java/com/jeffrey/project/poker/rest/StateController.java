package com.jeffrey.project.poker.rest;

import java.util.List;

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

import com.jeffrey.project.poker.exceptions.InvalidGameStateException;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter.JsonFriendlyGameState;
import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.player.Player;

@RestController
@CrossOrigin
public class StateController {

	private static final Logger logger = LoggerFactory.getLogger(StateController.class);

	@Autowired
	GameState gameState;

	@Autowired
	JsonFriendlyConverter jsonFriendlyConverter;

	@GetMapping(value = "/state/getPlayers", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Player> getAllPlayers() {
		return gameState.getAllPlayers();
	}


	@GetMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentState() {
		ResponseEntity<?> response;
		
		try {
			if(gameState == null || gameState.getPlayersList() == null  || gameState.getAllPlayers().size() < 1) {
				throw new InvalidGameStateException();
			}
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState, HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@GetMapping(value = "/state/{playerName}/{token}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCurrentState(@PathVariable String playerName, @PathVariable String token) {
		
		ResponseEntity<?> response;
		
		try {
			JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
			jsonFriendlyGameState.hideOtherPlayersCards(playerName);
			response = new ResponseEntity<JsonFriendlyGameState>(jsonFriendlyGameState, HttpStatus.ACCEPTED);
		} catch(Exception ex) {
			logger.error(ex.getMessage());
			response = new ResponseEntity<String>(ex.getMessage(), HttpStatus.FORBIDDEN);
		}
		
		return response;
	}

}
