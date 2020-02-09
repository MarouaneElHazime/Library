package fr.d2factory.libraryapp.builders;

import java.time.LocalDate;

import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.BorrowedBookDetails;

/**
 * @author ELHAZIME Marouane
 *
 * The builder of {@link BorrowedBookDetails}
 */
public class BorrowedBookDetailsBuilder {

	private Book book;

	private LocalDate borrowingDate;

	private LocalDate returningDate;

	
	public BorrowedBookDetailsBuilder setBook(Book book) {
		this.book = book;
		return this;
	}

	public BorrowedBookDetailsBuilder setBorrowingDate(LocalDate borrowingDate) {
		this.borrowingDate = borrowingDate;
		return this;
	}

	public BorrowedBookDetailsBuilder setReturningDate(LocalDate returningDate) {
		this.returningDate = returningDate;
		return this;
	}

	public BorrowedBookDetails build() {
		BorrowedBookDetails borrowedBookHistory = new BorrowedBookDetails();
		borrowedBookHistory.setBook(book);
		borrowedBookHistory.setBorrowingDate(borrowingDate);
		borrowedBookHistory.setReturningDate(returningDate);

		return borrowedBookHistory;
	}
	
	public static BorrowedBookDetailsBuilder getInstance() {
		return new BorrowedBookDetailsBuilder() ;
	} 

}
