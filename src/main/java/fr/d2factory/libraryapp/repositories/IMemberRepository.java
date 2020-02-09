package fr.d2factory.libraryapp.repositories;

import java.util.List;

import fr.d2factory.libraryapp.base.IAbstractRepository;
import fr.d2factory.libraryapp.entities.Member;

public interface IMemberRepository<T extends Member> extends IAbstractRepository<T, String>{

	/**
	 * add a list of members to the database
	 * 
	 * @param members list of members
	 */
	void add(List<T> members);

	/**
	 * add on member if dosen't exist
	 *  
	 * @param member member
	 */
	void add(T member);


}
