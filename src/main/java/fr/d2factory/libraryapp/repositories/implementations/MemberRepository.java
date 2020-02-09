package fr.d2factory.libraryapp.repositories.implementations;

import java.util.List;
import java.util.Optional;

import fr.d2factory.libraryapp.base.implementations.AbstractRepository;
import fr.d2factory.libraryapp.entities.Member;
import fr.d2factory.libraryapp.repositories.IMemberRepository;

/**
 * @author ELHAZIME Marouane
 * 
 * 
 *         The member repository emulates a database via HashMaps
 */
public class MemberRepository<T extends Member> extends AbstractRepository<T, String> implements IMemberRepository<T> {

	/**
	 * add a list of members to the database
	 * 
	 * @param members list of members
	 */
	@Override
	public void add(List<T> members) {
		if (members != null) {
			members.forEach(member -> getDataBase().computeIfAbsent(member.getId(), key -> member));
		}
	}

	/**
	 * add one member if dosen't exist
	 * 
	 * @param member member
	 */
	@Override
	public void add(T member) {
		getDataBase().computeIfAbsent(member.getId(), key -> member);
	}

	/**
	 * find member by id if exist
	 * 
	 * @param id member id
	 * @return member
	 */
	@Override
	public Optional<T> findByID(String id) {
		return Optional.of(getDataBase().get(id));
	}

}
