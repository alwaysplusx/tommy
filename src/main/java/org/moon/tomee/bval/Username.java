package org.moon.tomee.bval;

import javax.validation.constraints.NotNull;

public class Username {

	@NotNull(message = "first name may not be null", groups = { FirstNameGroup.class })
	private String firstName;
	@NotNull(message = "middle name may not be null", groups = { MiddleNameGroup.class })
	private String middleName;
	@NotNull(message = "last name may not be null", groups = { LastNameGroup.class })
	private String lastName;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
