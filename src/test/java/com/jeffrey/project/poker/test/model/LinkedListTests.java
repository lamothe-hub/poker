package com.jeffrey.project.poker.test.model;

import java.util.List;

import org.junit.Test;

import com.jeffrey.project.poker.model.player.PlayersList;

public class LinkedListTests {

	
	@Test
	public void test_playersListConstructor() {
		PlayersList testList = new PlayersList();
		assert(testList != null);
		assert(testList.getMaster() == null);
		assert(testList.getDealer() == null);
	}
	
	@Test
	public void test_addFirstPlayer() {
		PlayersList testList = new PlayersList();
		testList.addPlayer("Jeffrey",  10.0);
		assert(testList.getMaster().getName().equals("Jeffrey"));
		assert(testList.getMaster().getChipCount() == 10.0);
		assert(testList.getMaster().next == testList.getDealer());
		assert(testList.getMaster().next != null);
	}
	
	@Test
	public void test_addMultiplePlayers() {
		PlayersList testList = new PlayersList();
		testList.addPlayer("Jeffrey",  10.0);
		testList.addPlayer("John", 15.0);
		testList.addPlayer("Mark", 20);
		assert(testList.getMaster().next.getName().equals("John"));
		assert(testList.getMaster().next.next.getName().equals("Mark"));
		assert(testList.getMaster().next.next.next.getName().equals("Jeffrey"));
	}

	@Test
	public void test_removeMaster() {
		PlayersList testList = new PlayersList();
		testList.addPlayer("Jeffrey",  10.0);
		testList.removePlayer("Jeffrey");
		assert(testList.getMaster() == null);
		assert(testList.getDealer() == null);
	}
	
	@Test
	public void test_getPlayerCount() {
		PlayersList testList = new PlayersList();
		assert(testList.getPlayerCount() == 0);
		
		testList.addPlayer("Jeffrey",  10.0);
		assert(testList.getPlayerCount() == 1);

		testList.addPlayer("John", 15.0);
		assert(testList.getPlayerCount() == 2);

		testList.addPlayer("Mark", 20);
		assert(testList.getPlayerCount() == 3);
	}
	
	@Test
	public void test_removeMultipleElements() {
		PlayersList testList = new PlayersList();
		testList.addPlayer("Jeffrey",  10.0);
		testList.addPlayer("Johnny", 15.0);
		testList.addPlayer("Mark", 20);
		assert(testList.getPlayerCount() == 3);
		testList.removePlayer("Mark");
		assert(testList.getPlayerCount() == 2);
		assert(testList.getMaster().next.next == testList.getMaster());
		assert(testList.getMaster().getName().equals("Jeffrey"));
		testList.removePlayer("Jeffrey");
		assert(testList.getPlayerCount() == 1);
		assert(testList.getMaster().getName().equals("Johnny"));
		assert(testList.getMaster().getName().equals("Johnny"));
		assert(testList.getMaster().next == testList.getMaster());
	}
	
	@Test 
	public void test_getPlayerOrder() {
		PlayersList testList = new PlayersList();
		testList.addPlayer("Jeffrey", 100);
		testList.addPlayer("Johnny", 100);
		testList.addPlayer("Mark", 100);
		List<String> playerOrder = testList.getPlayerOrder("Jeffrey");
		assert(playerOrder.get(0).equals("Jeffrey") & playerOrder.get(1).equals("Johnny") & playerOrder.get(2).equals("Mark"));
		playerOrder = testList.getPlayerOrder("Johnny");
		System.out.println(playerOrder);
		assert(playerOrder.get(0).equals("Johnny") & playerOrder.get(1).equals("Mark") & playerOrder.get(2).equals("Jeffrey"));
		playerOrder = testList.getPlayerOrder("Mark");
		assert(playerOrder.get(0).equals("Mark") & playerOrder.get(1).equals("Jeffrey") & playerOrder.get(2).equals("Johnny"));

	
	}

}
