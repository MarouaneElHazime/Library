package fr.d2factory.libraryapp.exceptions;

/**
 * @author ELHAZIME Marouane
 * 
 *         The template exception for all business exception
 *
 */
public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8520757157401073282L;
	
	private String message ;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}
	
	
}
