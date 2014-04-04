package org.moon.tomee.bval;

import org.moon.tomee.bval.constraints.IDCard;

public class Citizen {

	@IDCard
	private String idCard;

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
}
