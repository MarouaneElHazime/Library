package fr.d2factory.libraryapp.exceptions;

/**
 * 
 * @author ELHAZIME Marouane
 * 
 *         This exception is thrown when the member try to return a book that he
 *         doesn't have
 */
public class BookAreNotBorrowedByTheMemberException extends BusinessException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8874657765905606054L;

	private String message;

	public BookAreNotBorrowedByTheMemberException() {
		super();
	}

	public BookAreNotBorrowedByTheMemberException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
