package fr.d2factory.libraryapp.services.implentations;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

import fr.d2factory.libraryapp.base.implementations.AbstractService;
import fr.d2factory.libraryapp.builders.BorrowedBookDetailsBuilder;
import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.BorrowedBookDetails;
import fr.d2factory.libraryapp.entities.ISBN;
import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.exceptions.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.exceptions.BookAreNotBorrowedByTheMemberException;
import fr.d2factory.libraryapp.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.exceptions.NotEnoughMoneyException;
import fr.d2factory.libraryapp.factories.ServiceFactory;
import fr.d2factory.libraryapp.repositories.IMemberRepository;
import fr.d2factory.libraryapp.repositories.implementations.MemberRepository;
import fr.d2factory.libraryapp.services.IBookService;
import fr.d2factory.libraryapp.services.IMemberService;

/**
 * @author ELHAZIME Marouane
 *
 *         Member service
 * 
 */
public abstract class MemberService<T extends Member> extends AbstractService<T> implements IMemberService<T> {

	/**
	 * book service
	 */
	private IBookService bookService = ServiceFactory.getBookService();

	protected IMemberRepository<T> memberRepository = new MemberRepository<T>();

	/**
	 * pay Book
	 * 
	 * @param member
	 * @param numberOfDays
	 * @throws NotEnoughMoneyException
	 */
	protected abstract void payBook(T member, int numberOfDays) throws NotEnoughMoneyException;

	/**
	 * get Number Days Before Late
	 * 
	 * @return number of days before the member be late
	 */
	protected abstract int getNumberDaysBeforeLate();

	/**
	 * check if the member are allowed to borrow books
	 * 
	 * @param member
	 * 
	 * @return true or false
	 */
	protected boolean isMemberAllowedToBorrowBook(T member) {

		int numberOfDaysBeforeLate = getNumberDaysBeforeLate();

		return member.getBorrowedBookDetails() == null || !member.getBorrowedBookDetails().stream()
				.anyMatch(book -> book.getReturningDate() == null && LocalDate.now().atStartOfDay()
						.isAfter(book.getBorrowingDate().plusDays(numberOfDaysBeforeLate).atStartOfDay()));

	}

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
	@Override
	public Book borrowBook(long isbnCode, T member, LocalDate borrowedAt)
			throws BookNotFoundException, BookAlreadyBorrowedException, HasLateBooksException {

		/* throw exception if the book not found or borrowed */
		Book book = bookService.getBookIfNotBorrowed(isbnCode);

		if (!isMemberAllowedToBorrowBook(member)) {
			throw new HasLateBooksException("Member Has Late Book : " + member.getId());
		}

		/* Marking the book as borrowed and adding the book detail to the member */

		book.setBorrowed(true);

		if (member.getBorrowedBookDetails() == null) {
			member.setBorrowedBookDetails(new ArrayList<BorrowedBookDetails>());
		}

		BorrowedBookDetails borrowedBookHistory = BorrowedBookDetailsBuilder.getInstance().setBook(book)
				.setBorrowingDate(borrowedAt).build();

		member.getBorrowedBookDetails().add(borrowedBookHistory);

		return book;

	}

	/**
	 * return book
	 * 
	 * @param book   {@link Book}
	 * @param member {@link Member}
	 * @throws BookAreNotBorrowedByTheMemberException
	 * @throws NotEnoughMoneyException
	 */
	@Override
	public void returnBook(Book book, T member) throws BookAreNotBorrowedByTheMemberException, NotEnoughMoneyException {

		/*
		 * check if the member have the book return true if the book exist in the set
		 * borrowedBookDetails and the returning date is null
		 */

		Optional<BorrowedBookDetails> borrowedBookDetailOptional = Optional.empty();
		if (member.getBorrowedBookDetails() != null) {
			borrowedBookDetailOptional = member.getBorrowedBookDetails().stream()
					.filter(borrowedBooksDetail -> borrowedBooksDetail.getBook() != null
							&& borrowedBooksDetail.getBook().equals(book)
							&& borrowedBooksDetail.getReturningDate() == null)
					.findFirst();
		}

		BorrowedBookDetails borrowedBookDetail = borrowedBookDetailOptional
				.orElseThrow(() -> new BookAreNotBorrowedByTheMemberException("The Book " + book.getIsbn().getIsbnCode()
						+ " are not borrowed by the member " + member.getId()));

		LocalDate now = LocalDate.now();

		int numberOfdays = (int) ChronoUnit.DAYS.between(borrowedBookDetail.getBorrowingDate(), now);

		/* the member pay acrroding to the number of days */
		payBook(member, numberOfdays);

		borrowedBookDetail.setReturningDate(LocalDate.now());

		book.setBorrowed(false);

	}

}
