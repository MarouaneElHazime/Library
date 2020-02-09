package fr.d2factory.libraryapp.factories;

import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.entities.Resident;
import fr.d2factory.libraryapp.entities.Student;
import fr.d2factory.libraryapp.repositories.IBookRepository;
import fr.d2factory.libraryapp.repositories.IMemberRepository;
import fr.d2factory.libraryapp.repositories.implementations.BookRepository;
import fr.d2factory.libraryapp.repositories.implementations.MemberRepository;

/**
 * @author ELHAZIME Marouane
 *
 *         Contains all repositories instances
 *
 */
public class RepositoryFactory {

	private static IMemberRepository<Member> memberRepository;

	private static IMemberRepository<Student> studentRepository;

	private static IMemberRepository<Resident> residentRepository;

	private static IBookRepository bookRepository;

	/**
	 * return memberRepository if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Member Repository
	 */
	public static IMemberRepository<Member> getMemberRepository() {
		return memberRepository != null ? memberRepository : (memberRepository = new MemberRepository<Member>());
	}

	/**
	 * return studentRepository if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Member Repository
	 */
	public static IMemberRepository<Student> getStudentRepository() {
		return studentRepository != null ? studentRepository : (studentRepository = new MemberRepository<Student>());
	}

	/**
	 * return residentRepository if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Member Repository
	 */
	public static IMemberRepository<Resident> getResidentRepository() {
		return residentRepository != null ? residentRepository
				: (residentRepository = new MemberRepository<Resident>());
	}

	/**
	 * return bookRepository if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Book Repository
	 */
	public static IBookRepository getBookRepository() {
		return bookRepository != null ? bookRepository : (bookRepository = new BookRepository());
	}
}
