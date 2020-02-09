package fr.d2factory.libraryapp.entities;

import java.util.List;

import fr.d2factory.libraryapp.base.MemberType;
import fr.d2factory.libraryapp.base.implementations.AbstractEntity;
import fr.d2factory.libraryapp.library.Library;

/**
 * A member is a person who can borrow and return books to a {@link Library} A
 * member can be either a student or a resident
 */
public abstract class Member extends AbstractEntity {

	
	/**
	 * member id 
	 */
	protected String id;
	
	/**
	 * An initial sum of money the member has
	 */
	protected float wallet;

	/**
	 * Member first name;
	 */
	protected String fisrtName;

	/**
	 * Member last name
	 */
	protected String lastName;
	
	/**
	 * The details of all books borrowed by the member
	 */
	protected List<BorrowedBookDetails> borrowedBookDetails;
	
	/**
	 * The type of the member 
	 * 
	 */
	protected MemberType memmberType ;
	
	/**
	 * The type of the member 
	 * 
	 * @return Member Type {@link MemberType}
	 */
	public abstract MemberType getType();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getWallet() {
		return wallet;
	}

	public void setWallet(float wallet) {
		this.wallet = wallet;
	}

	public String getFisrtName() {
		return fisrtName;
	}

	public void setFisrtName(String fisrtName) {
		this.fisrtName = fisrtName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<BorrowedBookDetails> getBorrowedBookDetails() {
		return borrowedBookDetails;
	}

	public void setBorrowedBookDetails(List<BorrowedBookDetails> borrowedBookDetails) {
		this.borrowedBookDetails = borrowedBookDetails;
	}


	
}
