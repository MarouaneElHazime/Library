package fr.d2factory.libraryapp.repositories;

import java.util.List;
import java.util.Optional;

import fr.d2factory.libraryapp.base.IAbstractRepository;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.ISBN;

public interface IBookRepository extends IAbstractRepository<Book, ISBN> {

	/**
	 * add a list of books
	 * 
	 * @param books list of books {@link book}
	 */
	void add(List<Book> books);

	/** 
	 *  add one book
	 * 
	 * @param book {@link book}
	 */
	void addBook(Book book);

	/**
	 * return book using isbn code
	 * 
	 * @param isbnCode isbn code
	 * @return book {@link book}
	 */
	Optional<Book> find(long isbnCode);

}
