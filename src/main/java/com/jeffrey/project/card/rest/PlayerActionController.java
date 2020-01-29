package com.jeffrey.project.card.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.project.card.logic.ActionValidator;
import com.jeffrey.project.card.logic.GameStateManager;
import com.jeffrey.project.card.logic.JsonFriendlyConverter;
import com.jeffrey.project.card.logic.JsonFriendlyConverter.JsonFriendlyGameState;

@RestController
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
    
	@GetMapping("/action/bet/{playerName}/{amount}")
	public String bet(@PathVariable String playerName, @PathVariable double amount) {
		String message = actionValidator.isValidBet(playerName, amount);
		return message;
	}
	
	@GetMapping("/action/call/{playerName}/{amount}")
	public String call(@PathVariable String playerName, @PathVariable double amount) {
		String message = actionValidator.isValidCall(playerName, amount);
		return message;
	}
	
	@GetMapping("/action/check/{playerName}")
	public String call(@PathVariable String playerName) {
		String message = actionValidator.isValidCheck(playerName);
		return message;
	}
    
}
