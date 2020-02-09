package fr.d2factory.libraryapp.library;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.BorrowedBookDetails;
import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.entities.Resident;
import fr.d2factory.libraryapp.entities.Student;
import fr.d2factory.libraryapp.exceptions.BookAlreadyBorrowedException;
import fr.d2factory.libraryapp.exceptions.BookAreNotBorrowedByTheMemberException;
import fr.d2factory.libraryapp.exceptions.BookNotFoundException;
import fr.d2factory.libraryapp.exceptions.BusinessException;
import fr.d2factory.libraryapp.exceptions.HasLateBooksException;
import fr.d2factory.libraryapp.exceptions.NotEnoughMoneyException;
import fr.d2factory.libraryapp.factories.RepositoryFactory;
import fr.d2factory.libraryapp.factories.ServiceFactory;
import fr.d2factory.libraryapp.repositories.IBookRepository;
import fr.d2factory.libraryapp.repositories.IMemberRepository;
import fr.d2factory.libraryapp.services.implentations.Params;

/**
 * Do not forget to consult the README.md :)
 */
public class LibraryTest {

	private Library library;

	private IBookRepository bookRepository;

	private IMemberRepository memberRepository;

	@BeforeEach
	void setup() throws JsonParseException, JsonMappingException, IOException {

		library = ServiceFactory.getLibrary();

		bookRepository = RepositoryFactory.getBookRepository();

		memberRepository = RepositoryFactory.getMemberRepository();

		ObjectMapper mapper = new ObjectMapper();

		File booksJson = new File("src/test/resources/books.json");
		List<Book> books = mapper.readValue(booksJson, new TypeReference<List<Book>>() {
		});

		File studentJson = new File("src/test/resources/students.json");
		List<Student> students = mapper.readValue(studentJson, new TypeReference<List<Student>>() {
		});

		File residentsJson = new File("src/test/resources/residents.json");
		List<Resident> residents = mapper.readValue(residentsJson, new TypeReference<List<Resident>>() {
		});

		bookRepository.add(books);

		memberRepository.add(students);

		memberRepository.add(residents);

	}

	@AfterEach
	@After
	void after() {
		bookRepository.clear();
		memberRepository.clear();
	}

	@Test
	void member_borrow_uknown_to_the_libary() {

		int isbnCode = -1;

		boolean isBookUknownToTheLibrary = !bookRepository.find(isbnCode).isPresent();

		assertTrue(isBookUknownToTheLibrary);

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Member> member = memberRepository.findByID("ME001");

		assertTrue(member.isPresent());

		Exception exception = assertThrows(BookNotFoundException.class, () -> {
			library.borrowBook(isbnCode, member.get(), LocalDate.now());
		});

		assertEquals("Book Not Found id : -1", exception.getMessage());

	}

	@Test
	void member_borrow_a_book_that_is_already_borrowed() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		long isbnCode = 46578964513L;

		Optional<Book> book = bookRepository.find(isbnCode);

		/* The book exits in data base */
		assertTrue(book.isPresent());

		/* The book is available */
		assertFalse(book.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Member> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		/* the member borrowing the book */
		library.borrowBook(isbnCode, memberME001.get(), LocalDate.now());

		/* The book is not available */
		assertTrue(book.get().isBorrowed());

		/*
		 * { "id": "AE004", "wallet": "2", "fisrtName": "Aziz", "lastName": "EL HAZIME"
		 * }
		 */
		Optional<Member> memberAE004 = memberRepository.findByID("AE004");

		/* The member AE004 try to borrow the same book as ME001 */

		Exception exception = assertThrows(BookAlreadyBorrowedException.class, () -> {
			library.borrowBook(isbnCode, memberAE004.get(), LocalDate.now());
		});

		assertEquals("Book Borrowed : 46578964513", exception.getMessage());
	}

	@Test
	void late_student_borrow_a_book() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> bookHarryPoter = bookRepository.find(46578964513L);
		/*
		 * { "title": "Around the world in 80 days", "author": "Jules Verne", "isbn": {
		 * "isbnCode": 3326456467846 } }
		 */
		Optional<Book> bookJulesVerne = bookRepository.find(3326456467846L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(bookHarryPoter.isPresent() && !bookHarryPoter.get().isBorrowed());

		/* The book 3326456467846 exits in data base and available */
		assertTrue(bookJulesVerne.isPresent() && !bookJulesVerne.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Student> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		/* the member borrow the book 46578964513L in date now - 31 */
		library.borrowBook(46578964513L, memberME001.get(),
				LocalDate.now().minusDays(Params.STUDENT_DAYS_BEFORE_LATE + 1));

		/* the member borrow the book 3326456467846 in date now */
		Exception exception = assertThrows(HasLateBooksException.class, () -> {
			library.borrowBook(3326456467846L, memberME001.get(), LocalDate.now());
		});

		assertEquals("Member Has Late Book : ME001", exception.getMessage());

	}

	@Test
	void late_resident_borrow_a_book() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> bookHarryPoter = bookRepository.find(46578964513L);
		/*
		 * { "title": "Around the world in 80 days", "author": "Jules Verne", "isbn": {
		 * "isbnCode": 3326456467846 } }
		 */
		Optional<Book> bookJulesVerne = bookRepository.find(3326456467846L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(bookHarryPoter.isPresent() && !bookHarryPoter.get().isBorrowed());

		/* The book 3326456467846 exits in data base and available */
		assertTrue(bookJulesVerne.isPresent() && !bookJulesVerne.get().isBorrowed());

		/*
		 * { "id": "AE004", "wallet": "2", "fisrtName": "Aziz", "lastName": "EL HAZIME"
		 * }
		 */
		Optional<Resident> memberAE004 = memberRepository.findByID("AE004");

		/* The member exits in data base */
		assertTrue(memberAE004.isPresent());

		/* the member borrow the book 46578964513L in date now - 61 */
		library.borrowBook(46578964513L, memberAE004.get(),
				LocalDate.now().minusDays(Params.RESIDENT_DAYS_BEFORE_LATE + 1));

		/* the member borrow the book 3326456467846 in date now */
		Exception exception = assertThrows(HasLateBooksException.class, () -> {
			library.borrowBook(3326456467846L, memberAE004.get(), LocalDate.now());
		});

		assertEquals("Member Has Late Book : AE004", exception.getMessage());

	}

	@Test
	void students_borrow_a_book() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Student> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		LocalDate date = LocalDate.now();

		/* the member borrow the book 46578964513L */
		library.borrowBook(46578964513L, memberME001.get(), date);

		List<BorrowedBookDetails> borrowedBooksDetails = memberME001.get().getBorrowedBookDetails();

		assertNotNull(borrowedBooksDetails);
		assertEquals(1, borrowedBooksDetails.size());

		BorrowedBookDetails borrowedBookDetails = borrowedBooksDetails.get(0);

		assertEquals(book.get(), borrowedBookDetails.getBook());
		assertEquals(date, borrowedBookDetails.getBorrowingDate());
		assertNull(borrowedBookDetails.getReturningDate());

	}

	@Test
	void resident_borrow_a_book() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "AE004", "wallet": "2", "fisrtName": "Aziz", "lastName": "EL HAZIME"
		 * }
		 */
		Optional<Resident> memberAE004 = memberRepository.findByID("AE004");

		/* The member exits in data base */
		assertTrue(memberAE004.isPresent());

		LocalDate date = LocalDate.now();

		/* the member borrow the book 46578964513L */
		library.borrowBook(46578964513L, memberAE004.get(), date);

		List<BorrowedBookDetails> borrowedBooksDetails = memberAE004.get().getBorrowedBookDetails();

		assertNotNull(borrowedBooksDetails);
		assertEquals(1, borrowedBooksDetails.size());

		BorrowedBookDetails borrowedBookDetails = borrowedBooksDetails.get(0);

		assertEquals(book.get(), borrowedBookDetails.getBook());
		assertEquals(date, borrowedBookDetails.getBorrowingDate());
		assertNull(borrowedBookDetails.getReturningDate());

	}

	@Test
	void member_borrow_two_books() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> bookHarryPoter = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(bookHarryPoter.isPresent() && !bookHarryPoter.get().isBorrowed());

		/*
		 * { "title": "Around the world in 80 days", "author": "Jules Verne", "isbn": {
		 * "isbnCode": 3326456467846 } }
		 */
		Optional<Book> bookJulesVerne = bookRepository.find(3326456467846L);

		/* The book 3326456467846 exits in data base and available */
		assertTrue(bookJulesVerne.isPresent() && !bookJulesVerne.get().isBorrowed());

		/*
		 * { "id": "OE002", "wallet": "2", "fisrtName": "Omar", "lastName": "EL HAZIME",
		 * "year": 6 }
		 */
		Optional<Member> memberOE002 = memberRepository.findByID("OE002");

		/* The member exits in data base */
		assertTrue(memberOE002.isPresent());

		/* the member borrow the book 46578964513L */

		LocalDate now = LocalDate.now();

		library.borrowBook(46578964513L, memberOE002.get(), now);

		library.borrowBook(3326456467846L, memberOE002.get(), LocalDate.now().plusDays(1));

		List<BorrowedBookDetails> borrowedBooksDetails = memberOE002.get().getBorrowedBookDetails();

		assertNotNull(borrowedBooksDetails);
		assertEquals(2, borrowedBooksDetails.size());

		BorrowedBookDetails borrowedBookHarryPoterDetails = borrowedBooksDetails.get(0);
		BorrowedBookDetails borrowedBookJulesDetails = borrowedBooksDetails.get(1);
		
		
		/* checking book 46578964513 detail */
		assertEquals(bookHarryPoter.get(), borrowedBookHarryPoterDetails.getBook());
		assertEquals(now, borrowedBookHarryPoterDetails.getBorrowingDate());
		assertNull(borrowedBookHarryPoterDetails.getReturningDate());

		/* checking book 3326456467846 detail */
		assertEquals(bookJulesVerne.get(), borrowedBookJulesDetails.getBook());
		assertEquals(now.plusDays(1), borrowedBookJulesDetails.getBorrowingDate());
		assertNull(borrowedBookJulesDetails.getReturningDate());

	}

	@Test
	void member_return_a_book_that_he_didnt_borrow() {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Member> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		Exception exception = assertThrows(BookAreNotBorrowedByTheMemberException.class, () -> {
			library.returnBook(book.get(), memberME001.get());
		});

		assertEquals("The Book 46578964513 are not borrowed by the member ME001", exception.getMessage());

	}

	@Test
	void student_has_not_enough_money_to_pay() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Student> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberME001.get(), LocalDate.now().minusDays(21));

		Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
			library.returnBook(book.get(), memberME001.get());
		});

		assertEquals("Student ME001 can't pay 2.10", exception.getMessage());
	}

	@Test
	void resident_has_not_enough_money_to_pay() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "AE004", "wallet": "2", "fisrtName": "Aziz", "lastName": "EL HAZIME"
		 * }
		 */
		Optional<Resident> memberAE004 = memberRepository.findByID("AE004");

		/* The member exits in data base */
		assertTrue(memberAE004.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberAE004.get(), LocalDate.now().minusDays(21));

		Exception exception = assertThrows(NotEnoughMoneyException.class, () -> {
			library.returnBook(book.get(), memberAE004.get());
		});

		assertEquals("Resident AE004 can't pay 2.10", exception.getMessage());
	}

	@Test
	void student_taxted_without_being_late() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "ME001", "wallet": "2", "fisrtName": "Marouane", "lastName":
		 * "EL HAZIME", "year": 5 }
		 */
		Optional<Student> memberME001 = memberRepository.findByID("ME001");

		/* The member exits in data base */
		assertTrue(memberME001.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberME001.get(), LocalDate.now().minusDays(10));

		float expectedwalletAfterTax = memberME001.get().getWallet() - 10 * Params.STUDENT_PRICE_UNIT;

		library.returnBook(book.get(), memberME001.get());
		
		assertFalse(book.get().isBorrowed()) ;


		assertEquals(expectedwalletAfterTax, memberME001.get().getWallet());

	}

	/*
	 * same as being late
	 */
	@Test
	void student_taxted_with_being_late() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "LP004", "wallet": "500", "fisrtName": "Lorem", "lastName": "Porem",
		 * "year": 7 }
		 */
		Optional<Student> memberLP004 = memberRepository.findByID("LP004");

		/* The member exits in data base */
		assertTrue(memberLP004.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberLP004.get(),
				LocalDate.now().minusDays(10 + Params.STUDENT_DAYS_BEFORE_LATE));

		float expectedwalletAfterTax = memberLP004.get().getWallet()
				- (10 + Params.STUDENT_DAYS_BEFORE_LATE) * Params.STUDENT_PRICE_UNIT;

		library.returnBook(book.get(), memberLP004.get());
		
		assertFalse(book.get().isBorrowed()) ;


		assertEquals(expectedwalletAfterTax, memberLP004.get().getWallet());
	}

	@Test
	void first_year_student_taxed_before_15_days() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "OE003", "wallet": "2", "fisrtName": "Oumayma", "lastName":
		 * "EL HAZIME", "year": 1 }
		 */
		Optional<Student> memberOE003 = memberRepository.findByID("OE003");

		/* The member exits in data base and first year */
		assertTrue(memberOE003.isPresent() && memberOE003.get().getYear() == 1);

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberOE003.get(), LocalDate.now().minusDays(10));

		float expectedwalletAfterTax = memberOE003.get().getWallet();

		library.returnBook(book.get(), memberOE003.get());
		
		assertFalse(book.get().isBorrowed()) ;

		assertEquals(expectedwalletAfterTax, memberOE003.get().getWallet());

	}

	@Test
	void first_year_student_taxed_after_15_days() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "OE003", "wallet": "2", "fisrtName": "Oumayma", "lastName":
		 * "EL HAZIME", "year": 1 }
		 */
		Optional<Student> memberOE003 = memberRepository.findByID("OE003");

		/* The member exits in data base and first year */
		assertTrue(memberOE003.isPresent() && memberOE003.get().getYear() == 1);

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberOE003.get(),
				LocalDate.now().minusDays(Params.STUDENT_FIRST_YEAR_NUMBER_FREE_DAYS + 5));

		float expectedwalletAfterTax = memberOE003.get().getWallet() - 5 * Params.STUDENT_PRICE_UNIT;

		library.returnBook(book.get(), memberOE003.get());
		
		assertFalse(book.get().isBorrowed()) ;


		assertEquals(expectedwalletAfterTax, memberOE003.get().getWallet());

	}

	@Test
	void resident_taxted_without_being_late() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "AE004", "wallet": "2", "fisrtName": "Aziz", "lastName": "EL HAZIME"
		 * }
		 */
		Optional<Resident> memberAE004 = memberRepository.findByID("AE004");

		/* The member exits in data base */
		assertTrue(memberAE004.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberAE004.get(), LocalDate.now().minusDays(10));

		float expectedwalletAfterTax = memberAE004.get().getWallet() - 10 * Params.RESIDENT_PRICE_UNIT_BEFOR_LATE;

		library.returnBook(book.get(), memberAE004.get());

		assertFalse(book.get().isBorrowed()) ;
		
		assertEquals(expectedwalletAfterTax, memberAE004.get().getWallet());

	}

	@Test
	void resident_taxted_after_being_late() throws BusinessException {
		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> book = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(book.isPresent() && !book.get().isBorrowed());

		/*
		 * { "id": "OE005", "wallet": "200", "fisrtName": "Aicha", "lastName":
		 * "BOURKADI" }
		 */
		Optional<Resident> memberAB005 = memberRepository.findByID("AB005");

		/* The member exits in data base */
		assertTrue(memberAB005.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberAB005.get(),
				LocalDate.now().minusDays(Params.RESIDENT_DAYS_BEFORE_LATE + 10));

		float expectedwalletAfterTax = memberAB005.get().getWallet()
				- Params.RESIDENT_DAYS_BEFORE_LATE * Params.RESIDENT_PRICE_UNIT_BEFOR_LATE
				- 10 * Params.RESIDENT_PRICE_UNIT_AFTER_LATE;

		library.returnBook(book.get(), memberAB005.get());
		
		assertFalse(book.get().isBorrowed()) ;


		assertEquals(expectedwalletAfterTax, memberAB005.get().getWallet());

	}

	@Test
	void late_member_return_book_and_borrow() throws BusinessException {

		/*
		 * { "title": "Harry Potter", "author": "J.K. Rowling", "isbn": { "isbnCode":
		 * 46578964513 } }
		 */
		Optional<Book> bookHarryPoter = bookRepository.find(46578964513L);

		/* The book 46578964513 exits in data base and available */
		assertTrue(bookHarryPoter.isPresent() && !bookHarryPoter.get().isBorrowed());

		/*
		 * { "id": "LP004", "wallet": "500", "fisrtName": "Lorem", "lastName": "Porem",
		 * "year": 7 }
		 */
		Optional<Student> memberLP004 = memberRepository.findByID("LP004");

		/* The member exits in data base */
		assertTrue(memberLP004.isPresent());

		/* borrow the book for 21 days */
		library.borrowBook(46578964513L, memberLP004.get(),
				LocalDate.now().minusDays(10 + Params.STUDENT_DAYS_BEFORE_LATE));
		
		/*book borrowed*/
		assertTrue(bookHarryPoter.get().isBorrowed());
		BorrowedBookDetails borrowedBookJulesDetails = memberLP004.get().getBorrowedBookDetails().get(0);
		assertEquals(bookHarryPoter.get(), borrowedBookJulesDetails.getBook());

		
		/*book returned*/
		library.returnBook(bookHarryPoter.get(), memberLP004.get());
		
		assertFalse(bookHarryPoter.get().isBorrowed());
		assertNotNull(borrowedBookJulesDetails.getReturningDate());


		
		/*
		 * { "title": "Around the world in 80 days", "author": "Jules Verne", "isbn": {
		 * "isbnCode": 3326456467846 } }
		 */
		Optional<Book> bookJules = bookRepository.find(3326456467846L);
		
		/* The book 46578964513 exits in data base and available */
		assertTrue(bookJules.isPresent() && !bookJules.get().isBorrowed());
		
		
		/*borrow the second book*/
		library.borrowBook(3326456467846L, memberLP004.get(),
				LocalDate.now());
		
		BorrowedBookDetails borrowedBookJules6Details = memberLP004.get().getBorrowedBookDetails().get(1);

		
		/*second book borrowed*/
		assertTrue(bookJules.get().isBorrowed());
		assertEquals(bookJules.get(), borrowedBookJules6Details.getBook());

	}

}
