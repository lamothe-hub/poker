package com.jeffrey.project.poker.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping(value = "/game/addPlayer/{name}/{chipCount}")
	public void addPlayer(@PathVariable("name") String name, @PathVariable("chipCount") int chipCount) {
		gameState.addPlayer(name, chipCount);
	}

	@GetMapping(value = "/state", produces = MediaType.APPLICATION_JSON_VALUE)
	public JsonFriendlyGameState getCurrentState() {
		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
		return jsonFriendlyGameState;
	}

}
