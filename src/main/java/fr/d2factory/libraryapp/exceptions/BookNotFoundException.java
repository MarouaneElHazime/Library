package fr.d2factory.libraryapp.exceptions;

/**
 * 
 * 
 * @author ELHAZIME Marouane
 * 
 *         This exception is thrown when a book is not found
 */
public class BookNotFoundException extends BusinessException {


	/**
	 * 
	 */
	private static final long serialVersionUID = 5731116727840333648L;
	
	
	private String message ;

	public BookNotFoundException() {
		super();
	}

	public BookNotFoundException(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}
	

}
