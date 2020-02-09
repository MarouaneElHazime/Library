package fr.d2factory.libraryapp.services.implentations;

import fr.d2factory.libraryapp.base.implementations.AbstractService;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.exceptions.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.factories.RepositoryFactory;
import fr.d2factory.libraryapp.repositories.IBookRepository;
import fr.d2factory.libraryapp.services.IBookService;

/**
 * @author ELHAZIME Marouane
 *
 * 
 *         Book Service
 *
 */
public class BookService extends AbstractService<Book> implements IBookService {

	/**
	 * book repository
	 */
	private IBookRepository bookRepository = RepositoryFactory.getBookRepository();

	/**
	 * get Book if found
	 * 
	 * @param isbnCode isbn code of the book {@link IsbnCode}
	 * @return book if found
	 * @throws BookNotFoundException 
	 */
	@Override
	public Book getBook(long isbnCode) throws BookNotFoundException {
		return bookRepository.find(isbnCode).orElseThrow(() -> new BookNotFoundException("Book Not Found id : " + isbnCode));
	}

	/**
	 * get Book if Not Borrowed
	 * 
	 * @param isbnCode isbn code of the book {@link IsbnCode}
	 * @return book if found and not borrowed
	 * @throws BookNotFoundException 
	 * @throws BookAlreadyBorrowedException 
	 */
	@Override
	public Book getBookIfNotBorrowed(long isbnCode) throws BookNotFoundException, BookAlreadyBorrowedException {

		/* if the book doesn't exist in the data base throw an exception */

		Book book = this.getBook(isbnCode);

		/* if the book is borrowed throw an exception */

		if (book.isBorrowed()) {
			throw new BookAlreadyBorrowedException("Book Borrowed : " + isbnCode);
		}

		return book;
	}



}
