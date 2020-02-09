package fr.d2factory.libraryapp.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import fr.d2factory.libraryapp.base.MemberType;

/**
 * @author ELHAZIME Marouane
 *
 *         Student is a Member
 *
 */
public class Student extends Member {

	private int year;

	@JsonCreator
	public Student(@JsonProperty("id") String id, @JsonProperty("wallet") float wallet,
			@JsonProperty("fisrtName") String fisrtName, @JsonProperty("lastName") String lastName,
			@JsonProperty("year") int year) {
		super();
		this.id = id;
		this.wallet = wallet;
		this.fisrtName = fisrtName;
		this.lastName = lastName;
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Override
	public MemberType getType() {
		return MemberType.STUDENT;
	}

}
