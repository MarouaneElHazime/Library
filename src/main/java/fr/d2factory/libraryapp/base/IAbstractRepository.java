package fr.d2factory.libraryapp.base;

import java.util.Optional;

import fr.d2factory.libraryapp.base.implementations.AbstractEntity;

/**
 * @author ELHAZIME Marouane
 * 
 *         the common Interface for All repositories
 *
 */
public interface IAbstractRepository<T extends AbstractEntity, ID> {

	Optional<T> findByID(ID id);
	
	void clear() ;

}
