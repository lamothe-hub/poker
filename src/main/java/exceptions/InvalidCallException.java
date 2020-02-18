package exceptions;

public class InvalidCallException extends RuntimeException {

	private static final long serialVersionUID = -5199241910251699088L;

	public InvalidCallException(String playerName, double attemptedAmount) {
		super(playerName + " can not call with amount: " + attemptedAmount);
	}
}
