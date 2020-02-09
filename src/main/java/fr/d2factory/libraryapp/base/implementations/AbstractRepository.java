package fr.d2factory.libraryapp.base.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.d2factory.libraryapp.base.IAbstractRepository;

/**
 * @author ELHAZIME Marouane
 * 
 *         The common template for all repositories.
 *
 * @param <T> Entity
 */
public abstract class AbstractRepository<T extends AbstractEntity, ID> implements IAbstractRepository<T, ID> {

	protected Map<ID, T> dataBase = new HashMap<ID, T>();

	@Override
	public Optional<T> findByID(ID id) {
		return Optional.ofNullable(dataBase.get(id));
	}
	
	@Override
	public void clear(){
		dataBase.clear();
	}

	public Map<ID, T> getDataBase() {
		return dataBase;
	}


	

}
