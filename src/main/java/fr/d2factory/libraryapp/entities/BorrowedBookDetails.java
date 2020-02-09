package fr.d2factory.libraryapp.entities;

import java.time.LocalDate;

import fr.d2factory.libraryapp.base.implementations.AbstractEntity;

/**
 * 
 * @author ELHAZIME Marouane
 *
 * Represent the relation between the member and the book
 */
public class BorrowedBookDetails extends AbstractEntity{
		
	private Book book;
	
	private LocalDate borrowingDate;
	
	private LocalDate returningDate; 

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public LocalDate getBorrowingDate() {
		return borrowingDate;
	}

	public void setBorrowingDate(LocalDate borrowingDate) {
		this.borrowingDate = borrowingDate;
	}

	public LocalDate getReturningDate() {
		return returningDate;
	}

	public void setReturningDate(LocalDate returningDate) {
		this.returningDate = returningDate;
	}
	
}
