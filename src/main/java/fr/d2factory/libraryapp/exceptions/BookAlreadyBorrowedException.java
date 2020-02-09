package fr.d2factory.libraryapp.exceptions;

/**
 * @author ELHAZIME Marouane
 * 
 *         This exception is thrown when a book is already borrowed
 */
public class BookAlreadyBorrowedException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -908064887207773189L;

	private String message ;

	public BookAlreadyBorrowedException() {
		super();
	}

	public BookAlreadyBorrowedException(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}

}
