package com.jeffrey.project.card.handchecker;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jeffrey.project.poker.handrank.HandChecker;
import com.jeffrey.project.poker.model.card.Card;
import com.jeffrey.project.poker.model.card.Hand;

public class HandCheckerTests {

	@Autowired
	HandChecker handChecker;
	
	@Test
	public void test() {
		// hand2 contains A of spades and 5 of spades
		Hand hand1 = new Hand(new Card(1, 2), new Card(5, 2));
	}

}
