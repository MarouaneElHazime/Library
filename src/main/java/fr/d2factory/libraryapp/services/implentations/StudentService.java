package fr.d2factory.libraryapp.services.implentations;

import fr.d2factory.libraryapp.entities.Student;
import fr.d2factory.libraryapp.exceptions.NotEnoughMoneyException;

public class StudentService extends MemberService<Student> {



	@Override
	public void payBook(Student student, int numberOfDays) throws NotEnoughMoneyException {

		if (student.getYear() == 1) {
			numberOfDays = numberOfDays > Params.STUDENT_FIRST_YEAR_NUMBER_FREE_DAYS ? numberOfDays - Params.STUDENT_FIRST_YEAR_NUMBER_FREE_DAYS : 0;
		}

		float price = Params.STUDENT_PRICE_UNIT * numberOfDays;

		if (student.getWallet() < price) {
			throw new NotEnoughMoneyException("Student " + student.getId() + " can't pay " + String.format("%.2f", price) );
		}

		student.setWallet(student.getWallet() - price);
	}

	@Override
	protected int getNumberDaysBeforeLate() {
		return Params.STUDENT_DAYS_BEFORE_LATE;
	}

}
