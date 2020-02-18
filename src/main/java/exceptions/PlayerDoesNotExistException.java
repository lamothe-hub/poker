package exceptions;

public class PlayerDoesNotExistException extends RuntimeException {


	private static final long serialVersionUID = 501883316371687875L;

	public PlayerDoesNotExistException(String playerName) {
		super("Player " + playerName + " does not exist!!!");
	}
	
	
}
