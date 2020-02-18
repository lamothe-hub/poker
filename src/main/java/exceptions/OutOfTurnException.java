package exceptions;

import com.jeffrey.project.card.model.player.Player;

public class OutOfTurnException extends RuntimeException {

	private static final long serialVersionUID = 2854203468903150891L;

	public OutOfTurnException(Player player) {
		super("It is not " + player.getName() + "'s turn.");
	}
	
	
	
}
