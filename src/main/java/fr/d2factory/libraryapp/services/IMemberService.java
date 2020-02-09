package fr.d2factory.libraryapp.services;

import java.time.LocalDate;

import fr.d2factory.libraryapp.base.IAbstractService;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.ISBN;
import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.exceptions.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.exceptions.BookAreNotBorrowedByTheMemberException;
import fr.d2factory.libraryapp.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.exceptions.NotEnoughMoneyException;

/**
 * @author ELHAZIME Marouane
 * 
 *         Interface Member Service
 * 
 */
public interface IMemberService<T extends Member> extends IAbstractService<T> {

	/**
	 * borrow book
	 * 
	 * @param isbnCode   isbn Code of the book {@link ISBN}
	 * @param member     {@link Member}
	 * @param borrowedAt locale date
	 * @return book {@link Book}
	 * @throws BookAlreadyBorrowedException 
	 * @throws BookNotFoundException 
	 * @throws HasLateBooksException 
	 */
	Book borrowBook(long isbnCode, T member, LocalDate borrowedAt) throws BookNotFoundException, BookAlreadyBorrowedException, HasLateBooksException;

	/**
	 * return book
	 * 
	 * @param book   {@link Book}
	 * @param member {@link Member}
	 * @throws BookAreNotBorrowedByTheMemberException 
	 * @throws NotEnoughMoneyException 
	 */
	void returnBook(Book book, T member) throws BookAreNotBorrowedByTheMemberException, NotEnoughMoneyException;

}
