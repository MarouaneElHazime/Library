package fr.d2factory.libraryapp.library;

import java.time.LocalDate;

import fr.d2factory.libraryapp.entities.Book;
import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.exceptions.BusinessException;
import fr.d2factory.libraryapp.factories.ServiceFactory;

public class LibraryImp implements Library {

	@Override
	public Book borrowBook(long isbnCode, Member member, LocalDate borrowedAt) throws BusinessException {
		return ServiceFactory.getMemeberServiceByMemberType(member.getType()).borrowBook(isbnCode, member, borrowedAt);
	}

	@Override
	public void returnBook(Book book, Member member) throws BusinessException {
		ServiceFactory.getMemeberServiceByMemberType(member.getType()).returnBook(book, member);
	}

}
