package fr.d2factory.libraryapp.repositories.implementations;

import java.util.List;
import java.util.Optional;

import fr.d2factory.libraryapp.base.implementations.AbstractRepository;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.ISBN;
import fr.d2factory.libraryapp.repositories.IBookRepository;

/**
 * The book repository emulates a database via HashMaps
 */
public class BookRepository extends AbstractRepository<Book, ISBN> implements IBookRepository {

	/**
	 * add a list of books
	 * 
	 * @param books list of books
	 */
	@Override
	public void add(List<Book> books) {
		if (books != null) {
			books.forEach(book -> getDataBase().computeIfAbsent(book.getIsbn(), key -> book));
		}
	}

	/**
	 * add one book
	 * 
	 * @param book {@link book}
	 */
	@Override
	public void addBook(Book book) {
		getDataBase().computeIfAbsent(book.getIsbn(), key -> book);
	}

	/**
	 * return book using isbn code
	 * 
	 * @param isbnCode isbn code
	 * @return book {@link book}
	 */
	@Override
	public Optional<Book> find(long isbnCode) {
		return super.findByID(new ISBN(isbnCode)) ;
	}

}
