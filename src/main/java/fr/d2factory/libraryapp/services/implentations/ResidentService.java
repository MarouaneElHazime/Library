package fr.d2factory.libraryapp.services.implentations;

import fr.d2factory.libraryapp.entities.Resident;
import fr.d2factory.libraryapp.exceptions.NotEnoughMoneyException;

/**
 * @author ELHAZIME Marouane
 *
 *         Resident Service
 */
public class ResidentService extends MemberService<Resident> {


	@Override
	public void payBook(Resident resident, int numberOfDays) throws NotEnoughMoneyException {

		int numbersOfLateDay = (numberOfDays > Params.RESIDENT_DAYS_BEFORE_LATE) ? numberOfDays - Params.RESIDENT_DAYS_BEFORE_LATE : 0;

		float price = numbersOfLateDay * Params.RESIDENT_PRICE_UNIT_AFTER_LATE + (numberOfDays - numbersOfLateDay)
				* Params.RESIDENT_PRICE_UNIT_BEFOR_LATE;

		if (resident.getWallet() < price) {
			throw new NotEnoughMoneyException("Resident " + resident.getId() + " can't pay " + String.format("%.2f", price) );
		}

		resident.setWallet(resident.getWallet() - price);
	}


	@Override
	protected int getNumberDaysBeforeLate() {
		return Params.RESIDENT_DAYS_BEFORE_LATE;
	}



}
