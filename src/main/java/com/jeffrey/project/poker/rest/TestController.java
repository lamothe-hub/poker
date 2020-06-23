package com.jeffrey.project.poker.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeffrey.project.poker.logic.JsonFriendlyConverter;
import com.jeffrey.project.poker.logic.JsonFriendlyConverter.JsonFriendlyGameState;
import com.jeffrey.project.poker.model.GameState;
import com.jeffrey.project.poker.model.TestPing;
import com.jeffrey.project.poker.model.card.Card;

@RestController
public class TestController {

	@Autowired
	GameState gameState;

	@Autowired
	JsonFriendlyConverter jsonFriendlyConverter;

	@GetMapping(value = "/hijared", produces = MediaType.APPLICATION_JSON_VALUE)
	public TestPing ping() {
		TestPing pingResponse = new TestPing("Hello Jared, this is a test message from the Java api!");
		return pingResponse;
	}

//	@GetMapping(value = "/sample", produces = MediaType.APPLICATION_JSON_VALUE)
//	public JsonFriendlyGameState getSampleGameState() {
//		System.out.println("Hello");
//		GameState sample = new GameState();
//		sample.setGameStatus("running");
//		sample.addPlayer("Jeffrey", 100);
//		sample.addPlayer("Johnny", 100);
//		sample.addPlayer("Mark", 100);
//		sample.addPlayer("Jared", 100);
//		System.out.println(sample.getPlayersList().getMaster().getCurrAmountInPot());
//		JsonFriendlyGameState test = jsonFriendlyConverter.convert(sample);
//		System.out.println(test.toString());
//		return test;
//	}
//
//	@GetMapping(value = "/sample2", produces = MediaType.APPLICATION_JSON_VALUE)
//	public GameState sampleGameState() {
//		GameState sample = new GameState();
//		sample.setGameStatus("running");
//		sample.addPlayer("Jeffrey", 100);
//		sample.addPlayer("Johnny", 200);
//		sample.addPlayer("Mark", 150);
//		return sample;
//	}
//
//	@GetMapping(value = "/createSample", produces = MediaType.APPLICATION_JSON_VALUE)
//	public JsonFriendlyGameState createSample() {
//		System.out.println("Hello");
//		gameState.setGameStatus("running");
//		gameState.addPlayer("Jeffrey", 100);
//		gameState.addPlayer("Johnny", 100);
//		gameState.addPlayer("Mark", 100);
//		gameState.addPlayer("Jared", 100);
//		JsonFriendlyGameState jsonFriendlyGameState = jsonFriendlyConverter.convert(gameState);
//		System.out.println(jsonFriendlyGameState.toString());
//		return jsonFriendlyGameState;
//	}

	@GetMapping(value = "/shuffle", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Card> shuffle() {
		return gameState.shuffle();
	}

	@GetMapping(value = "/deck", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Card> getDeck() {
		return gameState.getDeck().getDeck();
	}

}
