package fr.d2factory.libraryapp.exceptions;

/**
 * @author ELHAZIME Marouane
 * 
 *         This exception is thrown when a member try to pay and hi doesn't have
 *         enough money in his wallet
 * 
 */
public class NotEnoughMoneyException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 237117272170753228L;
	

	private String message ;

	public NotEnoughMoneyException() {
		super();
	}

	public NotEnoughMoneyException(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}

}
