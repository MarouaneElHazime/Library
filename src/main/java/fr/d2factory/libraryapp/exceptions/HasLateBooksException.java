package fr.d2factory.libraryapp.exceptions;

/**
 * 
 * This exception is thrown when a member who owns late books tries to borrow another book
 */
public class HasLateBooksException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4854153505885722467L;
	
	private String message ;

	public HasLateBooksException() {
		super();
	}

	public HasLateBooksException(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}
}
