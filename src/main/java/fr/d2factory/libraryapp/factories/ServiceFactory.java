package fr.d2factory.libraryapp.factories;

import fr.d2factory.libraryapp.base.MemberType;
import fr.d2factory.libraryapp.entities.Resident;
import fr.d2factory.libraryapp.entities.Student;
import fr.d2factory.libraryapp.exceptions.UkonwnMemberService;
import fr.d2factory.libraryapp.library.Library;
import fr.d2factory.libraryapp.library.LibraryImp;
import fr.d2factory.libraryapp.services.IBookService;
import fr.d2factory.libraryapp.services.IMemberService;
import fr.d2factory.libraryapp.services.implentations.BookService;
import fr.d2factory.libraryapp.services.implentations.ResidentService;
import fr.d2factory.libraryapp.services.implentations.StudentService;

/**
 * @author ELHAZIME Marouane
 * 
 *         Contains all services instances
 */
public class ServiceFactory {

	private static IMemberService<Student> studentService;

	private static IMemberService<Resident> residentService;

	private static IBookService bookService;

	private static Library library;

	/**
	 * return studentService if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Student Service
	 */
	public static IMemberService<Student> getStudentService() {
		return studentService == null ? studentService = new StudentService() : studentService;
	}

	/**
	 * return residentService if different from null otherwise instantiate it and
	 * return it
	 * 
	 * @return Resident Service
	 */
	public static IMemberService<Resident> getResidentService() {
		return residentService == null ? residentService = new ResidentService() : residentService;
	}

	/**
	 * return bookService if different from null otherwise instantiate it and return
	 * it
	 * 
	 * @return Book Service
	 */
	public static IBookService getBookService() {
		return bookService == null ? bookService = new BookService() : bookService;
	}

	/**
	 * return an instance of member service using the member type
	 * 
	 * @return Member Service
	 */
	public static IMemberService getMemeberServiceByMemberType(MemberType memberType) {

		switch (memberType) {

		case RESIDENT:

			return getResidentService();

		case STUDENT:

			return getStudentService();
		}

		throw new UkonwnMemberService("Member Type : " + memberType) ;

	}

	/**
	 * return library if different from null otherwise instantiate it and return it
	 * 
	 * @return library implementation
	 */
	public static Library getLibrary() {
		return library == null ? library = new LibraryImp() : library;
	}

}
