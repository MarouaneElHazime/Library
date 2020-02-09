package fr.d2factory.libraryapp.services;

import fr.d2factory.libraryapp.base.IAbstractService;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.exceptions.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.exceptions.BookNotFoundException;

/**
 * @author ELHAZIME Marouane
 *
 */
public interface IBookService extends IAbstractService<Book> {

	/**
	 * get tBook If Not Borrowed
	 * 
	 * @param isbnCode isbn code of the book {@link IsbnCode}
	 * @return book if found and not borrowed
	 * @throws BookNotFoundException 
	 * @throws BookAlreadyBorrowedException 
	 */
	Book getBookIfNotBorrowed(long isbnCode) throws BookNotFoundException, BookAlreadyBorrowedException;

	/**
	 * get Book if found
	 * 
	 * @param isbnCode isbn code of the book {@link IsbnCode}
	 * @return book if found
	 * @throws BookNotFoundException 
	 */
	Book getBook(long isbnCode) throws BookNotFoundException;


}
