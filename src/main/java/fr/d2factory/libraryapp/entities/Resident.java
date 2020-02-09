package fr.d2factory.libraryapp.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.d2factory.libraryapp.base.MemberType;

/**
 * @author ELHAZIME Marouane
 * 
 *         Resident is a Members
 *
 */
public class Resident extends Member {

	@JsonCreator
	public Resident(@JsonProperty("id") String id, @JsonProperty("wallet") float wallet,
			@JsonProperty("fisrtName") String fisrtName, @JsonProperty("lastName") String lastName) {
		super();
		this.id = id;
		this.wallet = wallet;
		this.fisrtName = fisrtName;
		this.lastName = lastName;
	}

	@Override
	public MemberType getType() {
		return MemberType.RESIDENT;
	}

}
