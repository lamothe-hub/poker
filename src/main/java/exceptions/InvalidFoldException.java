package exceptions;

public class InvalidFoldException extends RuntimeException {

	public InvalidFoldException(String playerName) {
		super(playerName + " can not fold right now.");
	}
}
