package fr.d2factory.libraryapp.exceptions;

public class UkonwnMemberService extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6908610896961113248L;
	
	private String message ;

	public UkonwnMemberService() {
		super();
	}

	public UkonwnMemberService(String message) {
		super(message);
		this.message = message ; 
	}

	public String getMessage() {
		return message;
	}

}
