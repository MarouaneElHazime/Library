package fr.d2factory.libraryapp.base.implementations;

import java.time.LocalDate;

/**
 * 
 * @author ELHAZIME Marouane 
 * 
 * The common template for all entities.
 * 
 */
public class AbstractEntity {

	/**
	 * Creation Date
	 */
	protected LocalDate createdAt;

	/**
	 * Modification Date ;
	 */
	protected LocalDate modifiedAt;

	/**
	 * The common constructor for all entities.
	 */
	protected AbstractEntity() {
		LocalDate now = LocalDate.now();
		createdAt = now;
		modifiedAt = now;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDate getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDate modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

}
