package com.jeffrey.project.card.model;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeffrey.project.card.logic.JsonFriendlyConverter;
import com.jeffrey.project.card.logic.JsonFriendlyConverter.JsonFriendlyGameState;

public class JsonFriendlyConverterTests {
	
	
	/*
	@Test 
	public void jsonFriendlyPlayerConvert_test() {
		Player testPlayer1 = new Player("Jeffrey", 100);
		Player testPlayer2 = new Player("Johnny", 50);
		
	}
*/
	@Test
	public void jsonFriendlyConverter_test() {
		JsonFriendlyConverter jsonFriendlyConverter = new JsonFriendlyConverter();

		GameState testState = new GameState(); 
		testState.addPlayer("Jeffrey",  100); 
		testState.addPlayer("Johnny",  100); 
		testState.addPlayer("Mark", 100);
		testState.setRunStatus("running");
		System.out.println("About to call the converter component");
		JsonFriendlyGameState test = jsonFriendlyConverter.convert(testState);
		System.out.println(test.getPlayersList());

		
	} 

}
