package fr.d2factory.libraryapp.base;

/**
 * @author ELHAZIME Marouane
 * 
 *  Type of members
 *
 */
public enum MemberType {

	RESIDENT(1),

	STUDENT(2);

	private int code;

	private MemberType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
